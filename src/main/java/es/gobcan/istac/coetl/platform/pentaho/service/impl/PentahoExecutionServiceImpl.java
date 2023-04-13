package es.gobcan.istac.coetl.platform.pentaho.service.impl;

import es.gobcan.istac.coetl.config.Constants;
import es.gobcan.istac.coetl.config.PentahoProperties;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.domain.Execution.Result;
import es.gobcan.istac.coetl.domain.Execution.Type;
import es.gobcan.istac.coetl.invocation.facade.NotificationRestInternalFacade;
import es.gobcan.istac.coetl.platform.common.service.GitService;
import es.gobcan.istac.coetl.platform.common.service.PlatformExecutionService;
import es.gobcan.istac.coetl.platform.pentaho.enumeration.JobMethodsEnum;
import es.gobcan.istac.coetl.platform.pentaho.enumeration.ServerMethodsEnum;
import es.gobcan.istac.coetl.platform.pentaho.enumeration.TransMethodsEnum;
import es.gobcan.istac.coetl.platform.pentaho.service.util.PentahoUtil;
import es.gobcan.istac.coetl.platform.pentaho.web.rest.dto.EtlStatusDTO;
import es.gobcan.istac.coetl.platform.pentaho.web.rest.dto.ServerStatusDTO;
import es.gobcan.istac.coetl.platform.pentaho.web.rest.dto.WebResultDTO;
import es.gobcan.istac.coetl.service.ExecutionService;
import es.gobcan.istac.coetl.service.ParameterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
public class PentahoExecutionServiceImpl implements PlatformExecutionService {

    private static final Logger LOG = LoggerFactory.getLogger(PentahoExecutionServiceImpl.class);
    private static final String TRANS_PREFIX_TAG_NAME = "transformation";
    private static final String JOB_PREFIX_TAG_NAME = "job";
    private static final String ERROR_PARSING_CARTE_WRAPPED_XML_TO_STRING_MESSAGE = "Error parsing Carte-wrapped XML to string";
    private static final String ERROR_CONNECTING_PENTAHO_SERVER_MESSAGE = "Error connecting to Pentaho server";
    private static final String[] PENTAHO_MESSAGE_PARAMETER = {"Pentaho"};

    private final ExecutionService executionService;

    private final ParameterService parameterService;

    private final MessageSource messageSource;

    private final GitService gitService;

    private final String url;
    private final String user;
    private final String password;
    
    private final NotificationRestInternalFacade notificationRestInternalFacade;

    public PentahoExecutionServiceImpl(PentahoProperties pentahoProperties, ExecutionService executionService, ParameterService parameterService, MessageSource messageSource, GitService gitService, NotificationRestInternalFacade notificationRestInternalFacade) {
        this.executionService = executionService;
        this.parameterService = parameterService;
        this.messageSource = messageSource;
        this.url = PentahoUtil.getUrl(pentahoProperties);
        this.user = PentahoUtil.getUser(pentahoProperties);
        this.password = PentahoUtil.getPassword(pentahoProperties);
        this.gitService = gitService;
        this.notificationRestInternalFacade = notificationRestInternalFacade;
    }

    @Override
    public Execution execute(Etl etl, Type type, String executor) {
        LOG.debug("Executing ETL : {}", etl.getCode());
        if (executionService.existsRunnnigOrWaitingByEtl(etl.getId())) {
            String duplicateEtlMessage = messageSource.getMessage("execution.note.duplicated", null, Constants.DEFAULT_LOCALE);
            return PentahoUtil.buildExecution(etl, type, executor, Result.DUPLICATED, duplicateEtlMessage);
        }

        final String etlFilename = gitService.getMainFileName(etl);

        WebResultDTO webResultDTO = registerETL(etl);
        if (!webResultDTO.isOk()) {
            return PentahoUtil.buildExecution(etl, type, executor, Result.FAILED, null, webResultDTO.getMessage());
        }

        String idExecution = webResultDTO.getId();

        if (etl.isTransformation()) {
            webResultDTO = executePrepareTrans(etlFilename, idExecution);
            if (!webResultDTO.isOk()) {
                executeRemoveTrans(etlFilename, idExecution);
                return PentahoUtil.buildExecution(etl, type, executor, Result.FAILED, idExecution, webResultDTO.getMessage());
            }
        }

        ServerStatusDTO serverStatusDTO = executeServerStatus();

        if (!serverStatusDTO.isOnline()) {
            notifyExecutionError(etl);
            String offlineServerMessage = messageSource.getMessage("execution.note.error.server.offline", PENTAHO_MESSAGE_PARAMETER, Constants.DEFAULT_LOCALE);
            return PentahoUtil.buildExecution(etl, type, executor, Result.FAILED, offlineServerMessage);
        }

        //@formatter:off
        List<EtlStatusDTO> transAndJobsRunningOrWaitingList = serverStatusDTO.getStatusList()
                .stream()
                .filter(etlInServer -> !etlInServer.getId().equals(idExecution))
                .filter(etlInServer -> (etlInServer.isRunning() || etlInServer.isWaiting()))
                .collect(Collectors.toList());
       //@formatter:on

        if (!transAndJobsRunningOrWaitingList.isEmpty()) {
            return PentahoUtil.buildExecution(etl, type, executor, Result.WAITING, idExecution);
        }

        webResultDTO = runEtl(etl, etlFilename, idExecution);

        if (!webResultDTO.isOk()) {
            notifyExecutionError(etl);
            removeEtl(etl, etlFilename, idExecution);
            return PentahoUtil.buildExecution(etl, type, executor, Result.FAILED, null, webResultDTO.getMessage());
        }

        return PentahoUtil.buildExecution(etl, type, executor, Result.RUNNING, idExecution);
    }

    @Override
    public WebResultDTO removeEtl(Etl etl, final String etlFilename, final String idExecution) {
        if (etl.isTransformation()) {
            return executeRemoveTrans(etlFilename, idExecution);
        } else {
            return executeRemoveJob(etlFilename, idExecution);
        }
    }

    @Override
    public WebResultDTO runEtl(Etl etl, final String etlFilename, final String idExecution) {
        if (etl.isTransformation()) {
            return executeStartTrans(etlFilename, idExecution);
        } else {
            return executeStartJob(etlFilename, idExecution);
        }
    }

    private WebResultDTO registerETL(Etl etl) {
        if (etl.isTransformation()) {
            return registerTrans(etl);
        } else {
            return registerJob(etl);
        }
    }

    private WebResultDTO registerTrans(Etl etl) {
        try {
            String mainCode = gitService.getMainFileContent(etl);
            String transCode = PentahoUtil.getCarteWrappedCodeFromEtlFile(mainCode, TRANS_PREFIX_TAG_NAME);
            String replacedTransCode = replaceEtlCodeVariables(etl, transCode);
            return executeRegisterTrans(replacedTransCode);
        } catch (SQLException | ParserConfigurationException | SAXException | IOException | TransformerException e) {
            LOG.error(ERROR_PARSING_CARTE_WRAPPED_XML_TO_STRING_MESSAGE, e);
            return buildErrorParseFileWebResult();
        } catch (RestClientException e) {
            LOG.error(ERROR_CONNECTING_PENTAHO_SERVER_MESSAGE, e);
            return buildErrorConnectionServerWebResult();
        }

    }

    private WebResultDTO registerJob(Etl etl) {
        try {
            String mainCode = gitService.getMainFileContent(etl);
            String jobCode = PentahoUtil.getCarteWrappedCodeFromEtlFile(mainCode, JOB_PREFIX_TAG_NAME);
            String replacedJobCode = replaceEtlCodeVariables(etl, jobCode);
            return executeRegisterJob(replacedJobCode);
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
        errorWebResultDTO.setResult(es.gobcan.istac.coetl.platform.pentaho.web.rest.dto.WebResultDTO.Result.ERROR);
        errorWebResultDTO.setMessage(messageSource.getMessage("execution.note.error.server.connection", PENTAHO_MESSAGE_PARAMETER, Constants.DEFAULT_LOCALE));
        return errorWebResultDTO;
    }

    private WebResultDTO buildErrorParseFileWebResult() {
        WebResultDTO errorWebResultDTO = new WebResultDTO();
        errorWebResultDTO.setResult(es.gobcan.istac.coetl.platform.pentaho.web.rest.dto.WebResultDTO.Result.ERROR);
        errorWebResultDTO.setMessage(messageSource.getMessage("execution.note.error.parsingXML", PENTAHO_MESSAGE_PARAMETER, Constants.DEFAULT_LOCALE));
        return errorWebResultDTO;
    }

    private WebResultDTO executeRegisterTrans(String codeEtl) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        return PentahoUtil.execute(user, password, url, TransMethodsEnum.REGISTER, HttpMethod.POST, codeEtl, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executePrepareTrans(String etlFilename, String idExecution) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        queryParams.add("id", idExecution);
        return PentahoUtil.execute(user, password, url, TransMethodsEnum.PREPARE, HttpMethod.GET, null, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executeStartTrans(String etlFilename, String idExecution) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        queryParams.add("level", "Debug");
        queryParams.add("id", idExecution);
        return PentahoUtil.execute(user, password, url, TransMethodsEnum.START, HttpMethod.GET, null, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executeRemoveTrans(String etlFilename, String idExecution) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        queryParams.add("id", idExecution);
        return PentahoUtil.execute(user, password, url, TransMethodsEnum.REMOVE, HttpMethod.GET, null, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executeRegisterJob(String codeEtl) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        return PentahoUtil.execute(user, password, url, JobMethodsEnum.REGISTER, HttpMethod.POST, codeEtl, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executeStartJob(String etlFilename, String idExecution) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        queryParams.add("id", idExecution);
        queryParams.add("level", "Debug");
        return PentahoUtil.execute(user, password, url, JobMethodsEnum.START, HttpMethod.GET, null, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executeRemoveJob(String etlFilename, String idExecution) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        queryParams.add("id", idExecution);
        return PentahoUtil.execute(user, password, url, JobMethodsEnum.REMOVE, HttpMethod.GET, null, queryParams, WebResultDTO.class).getBody();
    }

    private ServerStatusDTO executeServerStatus() {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        return PentahoUtil.execute(user, password, url, ServerMethodsEnum.STATUS, HttpMethod.GET, null, queryParams, ServerStatusDTO.class).getBody();
    }

    private String replaceEtlCodeVariables(Etl etl, String transCode) {
        Map<String, String> parameters = parameterService.findAllByEtlIdAsMap(etl.getId());
        String replacedTransCode = transCode;
        for (Entry<String, String> parameter : parameters.entrySet()) {
            replacedTransCode = replacedTransCode.replace("${".concat(parameter.getKey()).concat("}"), parameter.getValue());
        }
        return replacedTransCode;
    }

    @Override
    public void notifyExecutionError(Etl etl) {
        notificationRestInternalFacade.sendExecutionErrorEtlNotice(etl);
    }
}
