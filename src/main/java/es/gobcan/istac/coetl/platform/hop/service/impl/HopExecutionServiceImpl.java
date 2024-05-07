package es.gobcan.istac.coetl.platform.hop.service.impl;

import es.gobcan.istac.coetl.config.ApacheHopProperties;
import es.gobcan.istac.coetl.config.Constants;
import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Execution;
import es.gobcan.istac.coetl.domain.Execution.Result;
import es.gobcan.istac.coetl.domain.Execution.Type;
import es.gobcan.istac.coetl.invocation.facade.NotificationRestInternalFacade;
import es.gobcan.istac.coetl.platform.common.service.GitService;
import es.gobcan.istac.coetl.platform.common.service.PlatformExecutionService;
import es.gobcan.istac.coetl.platform.hop.enumeration.WorkflowMethodsEnum;
import es.gobcan.istac.coetl.platform.hop.enumeration.ServerMethodsEnum;
import es.gobcan.istac.coetl.platform.hop.enumeration.PipelineMethodsEnum;
import es.gobcan.istac.coetl.platform.hop.service.util.HopUtil;
import es.gobcan.istac.coetl.platform.hop.web.rest.dto.EtlStatusDTO;
import es.gobcan.istac.coetl.platform.hop.web.rest.dto.ServerStatusDTO;
import es.gobcan.istac.coetl.platform.hop.web.rest.dto.WebResultDTO;
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
public class HopExecutionServiceImpl implements PlatformExecutionService {

    private static final Logger LOG = LoggerFactory.getLogger(HopExecutionServiceImpl.class);
    private static final String PIPELINE_PREFIX_TAG_NAME = "pipeline";
    private static final String WORKFLOW_PREFIX_TAG_NAME = "workflow";
    private static final String ERROR_PARSING_APACHE_HOP_WRAPPED_XML_TO_STRING_MESSAGE = "Error parsing Apache Hop wrapped XML to string";
    private static final String ERROR_CONNECTING_APACHE_HOP_SERVER_MESSAGE = "Error connecting to Apache Hop server";
    private static final String[] HOP_MESSAGE_PARAMETER = {"Hop"};

    private final ExecutionService executionService;

    private final ParameterService parameterService;

    private final MessageSource messageSource;

    private final GitService gitService;

    private final String url;
    private final String user;
    private final String password;
    private final String jsonMetadata;
    
    private final NotificationRestInternalFacade notificationRestInternalFacade;

    public HopExecutionServiceImpl(ApacheHopProperties hopProperties, ExecutionService executionService, ParameterService parameterService, MessageSource messageSource, GitService gitService, NotificationRestInternalFacade notificationRestInternalFacade) {
        this.executionService = executionService;
        this.parameterService = parameterService;
        this.messageSource = messageSource;
        this.url = HopUtil.getUrl(hopProperties);
        this.user = HopUtil.getUser(hopProperties);
        this.password = HopUtil.getPassword(hopProperties);
        this.jsonMetadata = HopUtil.getJsonMetadata(hopProperties);
        this.gitService = gitService;
        this.notificationRestInternalFacade = notificationRestInternalFacade;
    }

    @Override
    public Execution execute(Etl etl, Type type, String executor) {
        LOG.debug("Executing ETL : {}", etl.getCode());
        if (executionService.existsRunnnigOrWaitingByEtl(etl.getId())) {
            String duplicateEtlMessage = messageSource.getMessage("execution.note.duplicated", null, Constants.DEFAULT_LOCALE);
            return HopUtil.buildExecution(etl, type, executor, Result.DUPLICATED, duplicateEtlMessage);
        }
        
        gitService.updateRepository(etl);

        final String etlFilename = gitService.getMainFileName(etl);

        WebResultDTO webResultDTO = registerETL(etl);
        if (!webResultDTO.isOk()) {
            return HopUtil.buildExecution(etl, type, executor, Result.FAILED, null, webResultDTO.getMessage());
        }

        String idExecution = webResultDTO.getId();

        if (etl.isPipeline()) {
            webResultDTO = executePreparePipeline(etlFilename, idExecution);
            if (!webResultDTO.isOk()) {
                executeRemovePipeline(etlFilename, idExecution);
                return HopUtil.buildExecution(etl, type, executor, Result.FAILED, idExecution, webResultDTO.getMessage());
            }
        }

        ServerStatusDTO serverStatusDTO = executeServerStatus();

        if (!serverStatusDTO.isOnline()) {
            notifyExecutionError(etl);
            String offlineServerMessage = messageSource.getMessage("execution.note.error.server.offline", HOP_MESSAGE_PARAMETER, Constants.DEFAULT_LOCALE);
            return HopUtil.buildExecution(etl, type, executor, Result.FAILED, offlineServerMessage);
        }

        //@formatter:off
        List<EtlStatusDTO> pipelineAndWorkflowRunningOrWaitingList = serverStatusDTO.getStatusList()
                .stream()
                .filter(etlInServer -> !etlInServer.getId().equals(idExecution))
                .filter(etlInServer -> (etlInServer.isRunning() || etlInServer.isWaiting()))
                .collect(Collectors.toList());
       //@formatter:on

        if (!pipelineAndWorkflowRunningOrWaitingList.isEmpty()) {
            return HopUtil.buildExecution(etl, type, executor, Result.WAITING, idExecution);
        }

        webResultDTO = runEtl(etl, etlFilename, idExecution);

        if (!webResultDTO.isOk()) {
            notifyExecutionError(etl);
            removeEtl(etl, etlFilename, idExecution);
            return HopUtil.buildExecution(etl, type, executor, Result.FAILED, null, webResultDTO.getMessage());
        }

        return HopUtil.buildExecution(etl, type, executor, Result.RUNNING, idExecution);
    }

    @Override
    public WebResultDTO removeEtl(Etl etl, final String etlFilename, final String idExecution) {
        if (etl.isPipeline()) {
            return executeRemovePipeline(etlFilename, idExecution);
        } else {
            return executeRemoveWorkflow(etlFilename, idExecution);
        }
    }

    @Override
    public WebResultDTO runEtl(Etl etl, final String etlFilename, final String idExecution) {
        if (etl.isPipeline()) {
            return executeStartPipeline(etlFilename, idExecution);
        } else {
            return executeStartWorkflow(etlFilename, idExecution);
        }
    }

    @Override
    public WebResultDTO registerETL(Etl etl) {
        if (etl.isPipeline()) {
            return registerPipeline(etl);
        } else {
            return registerWorkflow(etl);
        }
    }

    private WebResultDTO registerPipeline(Etl etl) {
        try {
            String mainCode = gitService.getMainFileContent(etl);
            String pipelineCode = HopUtil.getApacheHopWrappedCodeFromEtlFile(mainCode, PIPELINE_PREFIX_TAG_NAME, jsonMetadata);
            String replacedPipelineCode = replaceEtlCodeVariables(etl, pipelineCode);
            return executeRegisterPipeline(replacedPipelineCode);
        } catch (SQLException | ParserConfigurationException | SAXException | IOException | TransformerException e) {
            LOG.error(ERROR_PARSING_APACHE_HOP_WRAPPED_XML_TO_STRING_MESSAGE, e);
            return buildErrorParseFileWebResult();
        } catch (RestClientException e) {
            LOG.error(ERROR_CONNECTING_APACHE_HOP_SERVER_MESSAGE, e);
            return buildErrorConnectionServerWebResult();
        }

    }

    private WebResultDTO registerWorkflow(Etl etl) {
        try {
            String mainCode = gitService.getMainFileContent(etl);
            String workflowCode = HopUtil.getApacheHopWrappedCodeFromEtlFile(mainCode, WORKFLOW_PREFIX_TAG_NAME, jsonMetadata);
            String replacedWorkflowCode = replaceEtlCodeVariables(etl, workflowCode);
            return executeRegisterWorkflow(replacedWorkflowCode);
        } catch (SQLException | ParserConfigurationException | SAXException | IOException | TransformerException e) {
            LOG.error(ERROR_PARSING_APACHE_HOP_WRAPPED_XML_TO_STRING_MESSAGE, e);
            return buildErrorParseFileWebResult();
        } catch (RestClientException e) {
            LOG.error(ERROR_CONNECTING_APACHE_HOP_SERVER_MESSAGE, e);
            return buildErrorConnectionServerWebResult();
        }

    }

    private WebResultDTO buildErrorConnectionServerWebResult() {
        WebResultDTO errorWebResultDTO = new WebResultDTO();
        errorWebResultDTO.setResult(es.gobcan.istac.coetl.platform.hop.web.rest.dto.WebResultDTO.Result.ERROR);
        errorWebResultDTO.setMessage(messageSource.getMessage("execution.note.error.server.connection", HOP_MESSAGE_PARAMETER, Constants.DEFAULT_LOCALE));
        return errorWebResultDTO;
    }

    private WebResultDTO buildErrorParseFileWebResult() {
        WebResultDTO errorWebResultDTO = new WebResultDTO();
        errorWebResultDTO.setResult(es.gobcan.istac.coetl.platform.hop.web.rest.dto.WebResultDTO.Result.ERROR);
        errorWebResultDTO.setMessage(messageSource.getMessage("execution.note.error.parsingXML", HOP_MESSAGE_PARAMETER, Constants.DEFAULT_LOCALE));
        return errorWebResultDTO;
    }

    private WebResultDTO executeRegisterPipeline(String codeEtl) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        return HopUtil.execute(user, password, url, PipelineMethodsEnum.REGISTER, HttpMethod.POST, codeEtl, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executePreparePipeline(String etlFilename, String idExecution) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        queryParams.add("id", idExecution);
        return HopUtil.execute(user, password, url, PipelineMethodsEnum.PREPARE, HttpMethod.GET, null, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executeStartPipeline(String etlFilename, String idExecution) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        queryParams.add("level", "Debug");
        queryParams.add("id", idExecution);
        return HopUtil.execute(user, password, url, PipelineMethodsEnum.START, HttpMethod.GET, null, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executeRemovePipeline(String etlFilename, String idExecution) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        queryParams.add("id", idExecution);
        return HopUtil.execute(user, password, url, PipelineMethodsEnum.REMOVE, HttpMethod.GET, null, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executeRegisterWorkflow(String codeEtl) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        return HopUtil.execute(user, password, url, WorkflowMethodsEnum.REGISTER, HttpMethod.POST, codeEtl, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executeStartWorkflow(String etlFilename, String idExecution) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        queryParams.add("id", idExecution);
        queryParams.add("level", "Debug");
        return HopUtil.execute(user, password, url, WorkflowMethodsEnum.START, HttpMethod.GET, null, queryParams, WebResultDTO.class).getBody();
    }

    private WebResultDTO executeRemoveWorkflow(String etlFilename, String idExecution) {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        queryParams.add("name", etlFilename);
        queryParams.add("id", idExecution);
        return HopUtil.execute(user, password, url, WorkflowMethodsEnum.REMOVE, HttpMethod.GET, null, queryParams, WebResultDTO.class).getBody();
    }

    private ServerStatusDTO executeServerStatus() {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("xml", "y");
        return HopUtil.execute(user, password, url, ServerMethodsEnum.STATUS, HttpMethod.GET, null, queryParams, ServerStatusDTO.class).getBody();
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
