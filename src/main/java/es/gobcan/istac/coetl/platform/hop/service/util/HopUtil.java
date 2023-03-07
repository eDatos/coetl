package es.gobcan.istac.coetl.platform.hop.service.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.Instant;

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

import es.gobcan.istac.coetl.config.ApacheHopProperties;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.domain.Execution.Result;
import es.gobcan.istac.coetl.domain.Execution.Type;
import es.gobcan.istac.coetl.platform.hop.enumeration.HopMethodsEnum;
import es.gobcan.istac.coetl.platform.hop.web.rest.dto.HopResponseDTO;

public final class HopUtil {

    // Node XML Constants
    private static final String SUFFIX_CONFIGURATION_TAGNAME = "_configuration";
    private static final String SUFFIX_EXEC_CONFIGURATION_TAGNAME = "_execution_configuration";
    private static final String LOG_LEVEL_TAGNAME = "log_level";
    private static final String SAFE_MODE_TAGNAME = "safe_mode";
    private static final String METASTORE_JSON = "metastore_json";

    // Node values XML Constants
    private static final String LOG_LEVEL_VALUE = "DEBUG";
    private static final String SAFE_MODE_VALUE = "Y";

    private HopUtil() {
    }

    public static <E extends Enum<E> & HopMethodsEnum, T extends HopResponseDTO> ResponseEntity<T> execute(String user, String password, String url, E hopMethod, HttpMethod httpMethod,
            String body, MultiValueMap<String, String> queryParams, Class<T> clazz) {
        String uri = new StringBuilder().append(url).append(hopMethod.getResource()).toString();
        String uriWithQueryParameters = UriComponentsBuilder.fromHttpUrl(uri).queryParams(queryParams).toUriString();
        HttpEntity<String> httpEntity = new HttpEntity<>(body, createHeaders(user, password));
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(uriWithQueryParameters, httpMethod, httpEntity, clazz);
    }

    public static String getUrl(ApacheHopProperties pentahoProperties) {
        return pentahoProperties.getEndpoint().endsWith("/") ? pentahoProperties.getEndpoint() : pentahoProperties.getEndpoint() + "/";
    }

    public static String getUser(ApacheHopProperties pentahoProperties) {
        return pentahoProperties.getAuth().getUser();
    }

    public static String getPassword(ApacheHopProperties pentahoProperties) {
        return pentahoProperties.getAuth().getPassword();
    }

    public static String getApacheHopWrappedCodeFromEtlFile(String mainCode, String prefixTagName) throws SQLException, ParserConfigurationException, SAXException, IOException, TransformerException {
        Document documentXML = buildApacheHopWrappedDocumentXmlFromEtlFile(mainCode, prefixTagName);

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

    public static Execution buildExecution(Etl etl, Type type, Result result, String idExecution) {
        return buildExecution(etl, type, result, idExecution, null);
    }

    public static Execution buildExecution(Etl etl, Type type, Result result, String idExecution, String notes) {
        Execution execution = new Execution();
        execution.setEtl(etl);
        execution.setType(type);
        execution.setResult(result);
        execution.setPlanningDate(Instant.now());
        execution.setNotes(notes);
        if (Result.RUNNING.equals(result)) {
            execution.setStartDate(Instant.now());
        }
        execution.setIdExecution(idExecution);
        return execution;
    }

    public static String normalizeEtlCode(String etlCode) {
        return etlCode.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }

    private static HttpHeaders createHeaders(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName(CharEncoding.UTF_8)));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        return headers;
    }

    private static Document buildApacheHopWrappedDocumentXmlFromEtlFile(String etlFileCode, String prefixTagName) throws SQLException, ParserConfigurationException, SAXException, IOException {
        final String hopWrappedRootTag = prefixTagName + SUFFIX_CONFIGURATION_TAGNAME;

        Document etlFileDocument = getDocumentXmlFromEtlCode(etlFileCode);

        if (hasCarteWrappedNode(etlFileDocument, hopWrappedRootTag)) {
            return etlFileDocument;
        }

        // See https://hop.apache.org/manual/latest/hop-server/rest-api.html#_register_pipeline
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document hopWrappedDocument = documentBuilder.newDocument();

        Element hopWrappedRootElement = hopWrappedDocument.createElement(hopWrappedRootTag);
        hopWrappedDocument.appendChild(hopWrappedRootElement);

        Element etlCodeRootElement = getCodeNodeElement(etlFileDocument);
        hopWrappedDocument.adoptNode(etlCodeRootElement);
        hopWrappedRootElement.appendChild(etlCodeRootElement);

        Element hopConfigurationElement = hopWrappedDocument.createElement(prefixTagName + SUFFIX_EXEC_CONFIGURATION_TAGNAME);
        hopWrappedRootElement.appendChild(hopConfigurationElement);

        Element logLevelConfigurationElement = hopWrappedDocument.createElement(LOG_LEVEL_TAGNAME);
        logLevelConfigurationElement.setTextContent(LOG_LEVEL_VALUE);
        hopConfigurationElement.appendChild(logLevelConfigurationElement);

        Element safeModeConfigurationElement = hopWrappedDocument.createElement(SAFE_MODE_TAGNAME);
        safeModeConfigurationElement.setTextContent(SAFE_MODE_VALUE);
        hopConfigurationElement.appendChild(safeModeConfigurationElement);
        
        Element metastoreConfigurationElement = hopWrappedDocument.createElement(METASTORE_JSON);
        //FIXME
        metastoreConfigurationElement.setTextContent("H4sICMUY9mMEAGFwcGxpY2F0aW9uAO1X224TMRD9lWifQCLkQmkCbyHZQiFNQrKAEEErZ9e7MfHaxvY2DRX/ztjeSy4UKBJvtFITj2fGx3M54956CstrLL3nn269LV4NhJigDHvPPe+RpxQdcpaQ1HvOckqt4IrHsJsgqvAjT0h+s5txqZ3+mivNnHWn23vcht8OiAVSastlDGIUZ4SBiHE2M7avwEI5Yw4oJImxf0OUJiyFfYGlJljVx1WiK6Q0lgZpCU04FP1O3xxZoGB4a84vT2LVzXK4tVlVkL5/Bj0iMCUMN8FgBVufQJYzopsaK+2WMl5lysSq/HbrLSbTDxfjwRvfLGKk0QopXMdQ0Dwl7BIuX2uCmLAY3wRoRbESKKpusUUSr3munLF1d6qEoggrFewESNoHUT8Mts8iuRMaxw2vjE+NqEC4YHybULTBsOEqAVzVJ2ktySrX2N303Ww2nQeLMLi88hfB4GoWjgbBIAw+znxw9RFcvH03DfxwMB6HF5f+eLQA8QTEleWL6XTsDyYndhfT+dAPL0f+JLgEyzmcMQ3H0w/+fDhY+IWX2dxf+PP3flh8jsIP0/koLDQ+mnPejsPhdDLxhwFI7vILYI79XvjzOfhbDF/5V4NwMrgye973R16GWI7oO0kP68buwnbxPcUMSxJ1wWKvMmbTRfBy7gOon5SGgKylEqvDEqlN7qqR+xcF5RGiZn1XdXRX+FkfJVH/HKFe0sVn0ar3LMKddnz+7AnqRM/q8nl69qR7XEIzdxPA/L+G7llDa8TUfh2J1BIRZGiTUL5tUp463okgb4jFEjUjzhiONOHM7TDMz740U4nEupkBN1MrNlTeNLkgEbZ0pSUclXCZFUkb5Vm2azyIeYNxvQa6fQjIEkJxgWTZegfmatkCiPDXMOCyZSpv4Zw+XgtqLTCNC5fXiOaGRqjj74VGOq+5u/CrUCYoDgGeQ2cM4EoaM+1q10NCUBIhc8Nl64viZlxgZuodqlbLHB9QtcxZM7IzKpfWxtwW9FPYnOdseLh1673AKBsRCRGcFS58q2v2NM7EmEeFro3G82XL/F4juWwlnMY2JNdtCMuX7Hwj0rjd7p2zKN6IpC/TJ/G2DT8pAA+WLdMNWsJ5EIxXXARlBtSLPEmwXJBvBWWzPFthGfIkNImHI5w4Qfo1knXNDIBlTA+CrztdX9BcrS8hnBKyYU3rzM9s0w4plBJW+6NABRySlRZobsSxoq3QgyC/R5JYBjLFVuXWFIkZWlhFkgij5xrgV/mY44xrG318YzgmBErkuQQuc8l+5FWCUCOZYh26PBjX+5vus96suCgUnNKQHMQEqiZ0Fyr50b5eROhM3MvhyAO0Ftr9Nhq3VSt3TkPxyPaILXLT56WqtDG4b+TGBrb5kmAcr1C0CZWrqKemBsGba7VS2tmXaWg1CEmY5gS2xsjmTfKtwrrSr7wkOMz2H31qzbdheWgl1VzwUEEGK0mK9BqCl2ENo7EkgqIekWUwRAvpHxaYzdRJnCA4qrGD9DdKVlANq0l3jS3R6wbAaCgN9Ilk7HYa0DSVdsPF2Ptesukhx5Yv5KNJurLhksXjt3fWOwPh4Yx33ry9AVBcB+WaZ3DPqLhmNStzBQ3tBrM5vYqZ5Lmh1GJ9nzG+irtP4rjfTiKg2pMD6vM9C/MmqJMzh7ztAlK+K+uYjMk1hhir4RpHG6MA4I517hAPoq85UeREAzpd6QGlQ/O6Twz7Y3WCbbiHIMG6QFYF50C/DvFxDGccpsvOKa041WUCz/u9O4BUHqAIFJx+VjIT28vyMcQZ57Rg+Hrk19q1sz3YtgIzzlIer07mvECAqF4itWNR83DK783F8uVQvyXuPSlLfqkZ4LctvN+m3V/0aYnpz/q01C769E9p8e9g/zvUn937qhnjhDCyl4CIcpVLg+DgH9cTJOVza1Q5KOhEYYEgAFyWNaRgrrON2XSUBLRuFt9/ACFOblHtDwAA");
        hopWrappedRootElement.appendChild(metastoreConfigurationElement);

        return hopWrappedDocument;
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
