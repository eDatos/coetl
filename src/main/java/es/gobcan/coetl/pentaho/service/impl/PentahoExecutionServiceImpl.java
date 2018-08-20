package es.gobcan.coetl.pentaho.service.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.xml.sax.SAXException;

import es.gobcan.coetl.config.Constants;
import es.gobcan.coetl.config.PentahoProperties;
import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.domain.Execution;
import es.gobcan.coetl.domain.Execution.Result;
import es.gobcan.coetl.domain.Execution.Type;
import es.gobcan.coetl.domain.File;
import es.gobcan.coetl.pentaho.dto.EtlStatusDTO;
import es.gobcan.coetl.pentaho.dto.ServerStatusDTO;
import es.gobcan.coetl.pentaho.dto.WebResultDTO;
import es.gobcan.coetl.pentaho.enumeration.JobMethodsEnum;
import es.gobcan.coetl.pentaho.enumeration.ServerMethodsEnum;
import es.gobcan.coetl.pentaho.enumeration.TransMethodsEnum;
import es.gobcan.coetl.pentaho.service.PentahoExecutionService;
import es.gobcan.coetl.pentaho.service.util.PentahoUtil;
import es.gobcan.coetl.service.ExecutionService;

@Service
public class PentahoExecutionServiceImpl implements PentahoExecutionService {

    private static final Logger LOG = LoggerFactory.getLogger(PentahoExecutionServiceImpl.class);
    private static final String TRANS_PREFIX_TAG_NAME = "transformation";
    private static final String JOB_PREFIX_TAG_NAME = "job";
    private static final String ERROR_PARSING_CARTE_WRAPPED_XML_TO_STRING_MESSAGE = "Error parsing Carte-wrapped XML to string";
    private static final String ERROR_CONNECTING_PENTAHO_SERVER_MESSAGE = "Error connecting to Pentaho server";

    private final ExecutionService executionService;

    private final MessageSource messageSource;

    private final String url;
    private final String user;
    private final String password;

    public PentahoExecutionServiceImpl(PentahoProperties pentahoProperties, ExecutionService executionService, MessageSource messageSource) {
        this.executionService = executionService;
        this.messageSource = messageSource;
        this.url = PentahoUtil.getUrl(pentahoProperties);
        this.user = PentahoUtil.getUser(pentahoProperties);
        this.password = PentahoUtil.getPassword(pentahoProperties);
    }

    @Override
    public Execution execute(Etl etl, Type type) {
        LOG.debug("Executing ETL : {}", etl.getCode());

        if (executionService.existsRunnnigOrWaitingByEtl(etl.getId())) {
            String duplicateEtlMessage = messageSource.getMessage("execution.note.duplicated", null, Constants.DEFAULT_LOCALE);
            return buildExecution(etl, type, Result.DUPLICATED, duplicateEtlMessage);
        }

        WebResultDTO webResultDTO;
        final File etlFile = etl.getEtlFile();
        if (etl.isEtl()) {
            webResultDTO = registerAndPrepareTrans(etlFile);
        } else {
            webResultDTO = registerJob(etlFile);
        }

        if (!webResultDTO.isOk()) {
            return buildExecution(etl, type, Result.FAILED, webResultDTO.getMessage());
        }

        ServerStatusDTO serverStatusDTO = executeServerStatus();

        if (!serverStatusDTO.isOnline()) {
            String offlineServerMessage = messageSource.getMessage("execution.note.server.offline", null, Constants.DEFAULT_LOCALE);
            return buildExecution(etl, type, Result.FAILED, offlineServerMessage);
        }

        final String etlFilename = PentahoUtil.getFileBasename(etl.getEtlFile().getName());
        //@formatter:off
        List<EtlStatusDTO> transAndJobsRunningOrWaitingList = serverStatusDTO.getStatusList()
                .stream()
                .filter(etlInServer -> !etlInServer.getName().equals(etlFilename))
                .filter(etlInServer -> (etlInServer.isRunning() || etlInServer.isWaiting()))
                .collect(Collectors.toList());
       //@formatter:on

        if (!transAndJobsRunningOrWaitingList.isEmpty()) {
            return buildExecution(etl, type, Result.WAITING);
        }

        if (etl.isEtl()) {
            webResultDTO = executeStartTrans(etlFilename);
        } else {
            webResultDTO = executeStartJob(etlFilename);
        }

        return (webResultDTO.isOk()) ? buildExecution(etl, type, Result.RUNNING) : buildExecution(etl, type, Result.FAILED);
    }

    private Execution buildExecution(Etl etl, Type type, Result result) {
        return buildExecution(etl, type, result, null);
    }

    private Execution buildExecution(Etl etl, Type type, Result result, String notes) {
        Execution execution = new Execution();
        execution.setEtl(etl);
        execution.setType(type);
        execution.setResult(result);
        execution.setPlanningDate(ZonedDateTime.now());
        execution.setNotes(notes);
        return execution;
    }

    private WebResultDTO registerAndPrepareTrans(File etlFile) {
        try {
            String transCode = PentahoUtil.getCarteWrappedCodeFromEtlFile(etlFile, TRANS_PREFIX_TAG_NAME);
            WebResultDTO webResultDTO = executeRegisterTrans(transCode);
            if (webResultDTO.isOk()) {
                final String etlFilename = PentahoUtil.getFileBasename(etlFile.getName());
                return executePrepareTrans(etlFilename);
            }
            return webResultDTO;
        } catch (SQLException | ParserConfigurationException | SAXException | IOException | TransformerException e) {
            LOG.error(ERROR_PARSING_CARTE_WRAPPED_XML_TO_STRING_MESSAGE, e);
            return buildErrorParseFileWebResult();
        } catch (RestClientException e) {
            LOG.error(ERROR_CONNECTING_PENTAHO_SERVER_MESSAGE, e);
            return buildErrorConnectionServerWebResult();
        }

    }

    private WebResultDTO registerJob(File etlFile) {
        try {
            String jobCode = PentahoUtil.getCarteWrappedCodeFromEtlFile(etlFile, JOB_PREFIX_TAG_NAME);
            return executeRegisterJob(jobCode);
        } catch (SQLException | ParserConfigurationException | SAXException | IOException | TransformerException e) {
            LOG.error(ERROR_PARSING_CARTE_WRAPPED_XML_TO_STRING_MESSAGE, e);
            return buildErrorParseFileWebResult();
        } catch (RestClientException e) {
            LOG.error(ERROR_CONNECTING_PENTAHO_SERVER_MESSAGE, e);
            return buildErrorConnectionServerWebResult();
        }

    }

    private WebResultDTO buildErrorConnectionServerWebResult() {
        WebResultDTO errorWebResultDTO = new WebResultDTO();
        errorWebResultDTO.setResult(es.gobcan.coetl.pentaho.dto.WebResultDTO.Result.ERROR);
        errorWebResultDTO.setMessage(messageSource.getMessage("execution.note.error.parsingXML", null, Constants.DEFAULT_LOCALE));
        return errorWebResultDTO;
    }

    private WebResultDTO buildErrorParseFileWebResult() {
        WebResultDTO errorWebResultDTO = new WebResultDTO();
        errorWebResultDTO.setResult(es.gobcan.coetl.pentaho.dto.WebResultDTO.Result.ERROR);
        errorWebResultDTO.setMessage(messageSource.getMessage("execution.note.error.server.connection", null, Constants.DEFAULT_LOCALE));
        return errorWebResultDTO;
    }

    private WebResultDTO executeRegisterTrans(String codeEtl) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        return PentahoUtil.execute(user, password, url, TransMethodsEnum.REGISTER, HttpMethod.POST, codeEtl, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executePrepareTrans(String etlFilename) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        return PentahoUtil.execute(user, password, url, TransMethodsEnum.PREPARE, HttpMethod.GET, null, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executeStartTrans(String etlFilename) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        return PentahoUtil.execute(user, password, url, TransMethodsEnum.START, HttpMethod.GET, null, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executeRegisterJob(String codeEtl) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        return PentahoUtil.execute(user, password, url, JobMethodsEnum.REGISTER, HttpMethod.POST, codeEtl, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executeStartJob(String etlFilename) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        return PentahoUtil.execute(user, password, url, JobMethodsEnum.START, HttpMethod.GET, null, queryParams, WebResultDTO.class).getBody();
    }

    private ServerStatusDTO executeServerStatus() {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        return PentahoUtil.execute(user, password, url, ServerMethodsEnum.STATUS, HttpMethod.GET, null, queryParams, ServerStatusDTO.class).getBody();
    }
}
