package es.gobcan.istac.coetl.platform.hop.service.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Map;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import es.gobcan.istac.coetl.config.ApacheHopProperties;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.domain.Execution.Result;
import es.gobcan.istac.coetl.domain.Execution.Type;
import es.gobcan.istac.coetl.domain.enumeration.LogLevel;
import es.gobcan.istac.coetl.platform.hop.enumeration.HopMethodsEnum;
import es.gobcan.istac.coetl.platform.hop.web.rest.dto.HopResponseDTO;
import es.gobcan.istac.coetl.platform.web.rest.converter.CustomJaxb2RootElementHttpMessageConverter;

public final class HopUtil {

    // Node XML Constants
    private static final String SUFFIX_CONFIGURATION_TAGNAME = "_configuration";
    private static final String SUFFIX_EXEC_CONFIGURATION_TAGNAME = "_execution_configuration";
    private static final String RUN_CONFIGURATION_TAGNAME = "run_configuration";
    private static final String LOG_LEVEL_TAGNAME = "log_level";
    private static final String SAFE_MODE_TAGNAME = "safe_mode";
    private static final String METASTORE_JSON = "metastore_json";

    // Node values XML Constants
    private static final String SAFE_MODE_VALUE = "Y";

    private static final String RUN_CONFIGURATION_VALUE = "local";
    
    // Variable placeholders
    private static final String ETL_CODE = "${ETL_CODE}";
    private static final String ETL_RESOURCES = "${ETL_RESOURCES}";
    private static final String HOP_FOLDER = "${HOP_FOLDER}";
    

    private HopUtil() {
    }

    public static <E extends Enum<E> & HopMethodsEnum, T extends HopResponseDTO> ResponseEntity<T> execute(String user, String password, String url, E hopMethod, HttpMethod httpMethod,
            String body, MultiValueMap<String, String> queryParams, Class<T> clazz) {
        String uri = new StringBuilder().append(url).append(hopMethod.getResource()).toString();
        String uriWithQueryParameters = UriComponentsBuilder.fromHttpUrl(uri).queryParams(queryParams).toUriString();
        HttpEntity<String> httpEntity = new HttpEntity<>(body, createHeaders(user, password));
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.getMessageConverters().add(new CustomJaxb2RootElementHttpMessageConverter());
        return restTemplate.exchange(uriWithQueryParameters, httpMethod, httpEntity, clazz);
    }

    public static String getUrl(ApacheHopProperties hopProperties) {
        return hopProperties.getEndpoint().endsWith("/") ? hopProperties.getEndpoint() : hopProperties.getEndpoint() + "/";
    }

    public static String getUser(ApacheHopProperties hopProperties) {
        return hopProperties.getAuth().getUser();
    }

    public static String getPassword(ApacheHopProperties hopProperties) {
        return hopProperties.getAuth().getPassword();
    }
    
    public static String getJsonMetadata(ApacheHopProperties hopProperties) {
        return hopProperties.getJsonMetadata();
    }
    
    public static String getVariablesTemplate(ApacheHopProperties hopProperties) {
        return hopProperties.getVariablesTemplate();
    }
    
    public static String getVariablesPlaceholdersReplaced(Etl etl, Map<String, String> params, ApacheHopProperties hopProperties) {
        String template = hopProperties.getVariablesTemplate();
        return template
                .replace(ETL_CODE, etl.getCode())
                .replace(ETL_RESOURCES, params.get("ETL_RESOURCES"))
                .replace(HOP_FOLDER, hopProperties.getHost().getHopFolder());
        
    }

    public static String getApacheHopWrappedCodeFromEtlFile(String mainCode, String prefixTagName, String jsonMetadata, String variables, LogLevel level) throws SQLException, ParserConfigurationException, SAXException, IOException, TransformerException {
        Document documentXML = buildApacheHopWrappedDocumentXmlFromEtlFile(mainCode, prefixTagName, jsonMetadata, variables, level);

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

    public static Execution buildExecution(Etl etl, Type type, String executor, Result result, String idExecution) {
        return buildExecution(etl, type, executor, result, idExecution, null);
    }

    public static Execution buildExecution(Etl etl, Type type, String executor, Result result, String idExecution, String notes) {
        Execution execution = new Execution();
        execution.setEtl(etl);
        execution.setType(type);
        execution.setExecutor(executor);
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
    
    public static String addMetadataToJsonMetadata(String jsonMetadata, Map<String, List<String>> metadataInfo) throws JSONException {
        JSONObject json = new JSONObject(jsonMetadata);
        
        for (Map.Entry<String, List<String>> entry : metadataInfo.entrySet()) {
            JSONArray metadata = json.optJSONArray(entry.getKey());
            if (metadata == null) {
                metadata = new JSONArray();
                json.put(entry.getKey(), metadata);
            }
            
            for (String value : entry.getValue()) {
                try {
                    metadata.put(new JSONObject(value));
                } catch (JSONException e) {
                    throw new JSONException("Error parsing JSON value for key " + entry.getKey() + ": " + value);
                }
            }
        }
        
        return json.toString();
    }

    private static HttpHeaders createHeaders(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName(CharEncoding.UTF_8)));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        return headers;
    }

    private static Document buildApacheHopWrappedDocumentXmlFromEtlFile(String etlFileCode, String prefixTagName, String jsonMetadata, String variables, LogLevel level)
            throws SQLException, ParserConfigurationException, SAXException, IOException {
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

        Element runConfigurationElement = hopWrappedDocument.createElement(RUN_CONFIGURATION_TAGNAME);
        runConfigurationElement.setTextContent(RUN_CONFIGURATION_VALUE);
        hopConfigurationElement.appendChild(runConfigurationElement);

        Element logLevelConfigurationElement = hopWrappedDocument.createElement(LOG_LEVEL_TAGNAME);
        logLevelConfigurationElement.setTextContent(level.getHopLogLevel());
        hopConfigurationElement.appendChild(logLevelConfigurationElement);

        Element safeModeConfigurationElement = hopWrappedDocument.createElement(SAFE_MODE_TAGNAME);
        safeModeConfigurationElement.setTextContent(SAFE_MODE_VALUE);
        hopConfigurationElement.appendChild(safeModeConfigurationElement);

        Element metastoreConfigurationElement = hopWrappedDocument.createElement(METASTORE_JSON);

        metastoreConfigurationElement.setTextContent(jsonMetadata);
        hopWrappedRootElement.appendChild(metastoreConfigurationElement);

        if (variables != null && !variables.trim().isEmpty()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document variablesDocument = builder.parse(new ByteArrayInputStream(variables.getBytes()));
            Node variablesRoot = variablesDocument.getDocumentElement();
            Node importedNode = hopWrappedDocument.importNode(variablesRoot, true);

            hopConfigurationElement.appendChild(importedNode);
        }

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
}
