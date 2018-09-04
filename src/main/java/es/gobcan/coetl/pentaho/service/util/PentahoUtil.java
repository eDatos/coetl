package es.gobcan.coetl.pentaho.service.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.ZonedDateTime;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import es.gobcan.coetl.config.PentahoProperties;
import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.domain.Execution;
import es.gobcan.coetl.domain.Execution.Result;
import es.gobcan.coetl.domain.Execution.Type;
import es.gobcan.coetl.domain.File;
import es.gobcan.coetl.pentaho.enumeration.CarteMethodsEnum;
import es.gobcan.coetl.pentaho.web.rest.dto.PentahoResponseDTO;

public final class PentahoUtil {

    // Node XML Constants
    private static final String SUFFIX_CONFIGURATION_TAGNAME = "_configuration";
    private static final String SUFFIX_EXEC_CONFIGURATION_TAGNAME = "_execution_configuration";
    private static final String LOG_LEVEL_TAGNAME = "log_level";
    private static final String SAFE_MODE_TAGNAME = "safe_mode";

    // Node values XML Constants
    private static final String LOG_LEVEL_VALUE = "DEBUG";
    private static final String SAFE_MODE_VALUE = "Y";

    private PentahoUtil() {
    }

    public static <E extends Enum<E> & CarteMethodsEnum, T extends PentahoResponseDTO> ResponseEntity<T> execute(String user, String password, String url, E pentahoMethod, HttpMethod httpMethod,
            String body, MultiValueMap<String, String> queryParams, Class<T> clazz) {
        String uri = new StringBuilder().append(url).append(pentahoMethod.getResource()).toString();
        String uriWithQueryParameters = UriComponentsBuilder.fromHttpUrl(uri).queryParams(queryParams).toUriString();
        HttpEntity<String> httpEntity = new HttpEntity<>(body, createHeaders(user, password));
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(uriWithQueryParameters, httpMethod, httpEntity, clazz);
    }

    public static String getUrl(PentahoProperties pentahoProperties) {
        //@formatter:off
        return new StringBuilder()
                .append("http://")
                .append(pentahoProperties.getEndpoint().endsWith("/") ? pentahoProperties.getEndpoint() : pentahoProperties.getEndpoint() + "/")
                .append("kettle/")
                .toString();
        //@formatter:on
    }

    public static String getUser(PentahoProperties pentahoProperties) {
        return pentahoProperties.getAuth().getUser();
    }

    public static String getPassword(PentahoProperties pentahoProperties) {
        return pentahoProperties.getAuth().getPassword();
    }

    public static String getCarteWrappedCodeFromEtlFile(File etlFile, String prefixTagName) throws SQLException, ParserConfigurationException, SAXException, IOException, TransformerException {
        Document documentXML = buildCarteWrappedDocumentXmlFromEtlFile(etlFile, prefixTagName);

        StringWriter sw = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        tf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.ENCODING, CharEncoding.UTF_8);

        transformer.transform(new DOMSource(documentXML), new StreamResult(sw));

        return sw.toString();
    }

    public static String getFileBasename(String fileNameWithExtension) {
        return FilenameUtils.getBaseName(fileNameWithExtension);
    }

    public static Execution buildExecution(Etl etl, Type type, Result result) {
        return buildExecution(etl, type, result, null);
    }

    public static Execution buildExecution(Etl etl, Type type, Result result, String notes) {
        Execution execution = new Execution();
        execution.setEtl(etl);
        execution.setType(type);
        execution.setResult(result);
        execution.setPlanningDate(ZonedDateTime.now());
        execution.setNotes(notes);
        return execution;
    }

    private static HttpHeaders createHeaders(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName(CharEncoding.UTF_8)));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        return headers;
    }

    private static Document buildCarteWrappedDocumentXmlFromEtlFile(File etlFile, String prefixTagName) throws SQLException, ParserConfigurationException, SAXException, IOException {
        String etlFileCode = convertBlobToString(etlFile.getData());
        final String carteWrappedRootTag = prefixTagName + SUFFIX_CONFIGURATION_TAGNAME;

        Document etlFileDocument = getDocumentXmlFromEtlCode(etlFileCode);

        if (hasCarteWrappedNode(etlFileDocument, carteWrappedRootTag)) {
            return etlFileDocument;
        }

        // We need to create carte configuration nodes, more info: https://community.hitachivantara.com/message/38652-sending-xml-job-to-carte
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document carteWrappedDocument = documentBuilder.newDocument();

        Element carteWrappedRootElement = carteWrappedDocument.createElement(carteWrappedRootTag);
        carteWrappedDocument.appendChild(carteWrappedRootElement);

        Element etlCodeRootElement = getCodeNodeElement(etlFileDocument);
        carteWrappedDocument.adoptNode(etlCodeRootElement);
        carteWrappedRootElement.appendChild(etlCodeRootElement);

        Element carteConfigurationElement = carteWrappedDocument.createElement(prefixTagName + SUFFIX_EXEC_CONFIGURATION_TAGNAME);
        carteWrappedRootElement.appendChild(carteConfigurationElement);

        Element logLevelConfigurationElement = carteWrappedDocument.createElement(LOG_LEVEL_TAGNAME);
        logLevelConfigurationElement.setTextContent(LOG_LEVEL_VALUE);
        carteConfigurationElement.appendChild(logLevelConfigurationElement);

        Element safeModeConfigurationElement = carteWrappedDocument.createElement(SAFE_MODE_TAGNAME);
        safeModeConfigurationElement.setTextContent(SAFE_MODE_VALUE);
        carteConfigurationElement.appendChild(safeModeConfigurationElement);

        return carteWrappedDocument;
    }

    private static Document getDocumentXmlFromEtlCode(String etlCode) throws SQLException, ParserConfigurationException, SAXException, IOException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(etlCode));
        inputSource.setEncoding(CharEncoding.UTF_8);
        return documentBuilder.parse(inputSource);
    }

    private static boolean hasCarteWrappedNode(Document etlFileDocument, String carteWrappedRootTag) {
        NodeList nodeList = etlFileDocument.getElementsByTagName(carteWrappedRootTag);
        return nodeList.getLength() > 0;
    }

    private static Element getCodeNodeElement(Document etlFileDocument) {

        return etlFileDocument.getDocumentElement();
    }

    private static String convertBlobToString(Blob data) throws SQLException {
        byte[] blobData = data.getBytes(1, (int) data.length());
        return new String(blobData);
    }

}
