package es.gobcan.istac.coetl.errors;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_ACCESS_DENIED = "error.accessDenied";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String ERR_METHOD_NOT_SUPPORTED = "error.methodNotSupported";
    public static final String ERR_INTERNAL_SERVER_ERROR = "error.internalServerError";
    public static final String ERR_FIELD_VALUE = "error.field.value";
    public static final String ERR_FIELD_VALIDATION = "error.field.validation";
    public static final String ERR_FIELD_CONSTRAINT = "error.field.constraint";

    public static final String USUARIO_EXISTE = "error.usuario-existe";
    public static final String USUARIO_LDAP_NO_ENCONTRADO = "error.userManagement.usuario-ldap-no-encontrado";
    public static final String USUARIO_NO_VALIDO = "error.userManagement.usuario-no-valido";

    public static final String FICHERO_UNICO_VACIO = "error.file.single.empty";
    public static final String FICHEROS_VARIOS_VACIOS = "error.file.multi.empty";
    public static final String FICHERO_NO_ENCONTRADO = "error.file-not-found";

    public static final String ENTIDAD_NO_ENCONTRADA = "error.entidad-no-encontrada";
    public static final String ID_EXISTE = "error.id-existe";
    public static final String ID_FALTA = "error.id-falta";
    public static final String ENTITY_DELETED = "error.entity.deleted";

    public static final String QUERY_NO_SOPORTADA = "error.query-no-soportada";

    // ETL
    public static final String ETL_EXECUTE_ERROR = "error.etl.executingError";
    public static final String ETL_CURRENTLY_DELETED = "error.etl.currentlyDeleted";
    public static final String ETL_CURRENTLY_NOT_DELETED = "error.etl.currentlyNotDeleted";
    public static final String ETL_FILE_CURRENTLY_DELETED = "error.etl.etlFileCurrentlyDeleted";
    public static final String ETL_CRON_EXPRESSION_NOT_VALID = "error.etl.cronExpressionNotValid";
    public static final String ETL_SCHEDULE_ERROR = "error.etl.scheduleError";
    public static final String ETL_UNSCHEDULE_ERROR = "error.etl.unscheduleError";
    public static final String ETL_CODE_EXISTS = "error.etl.codeExists";
    public static final String ETL_ATTACHED_FILES_UPLOAD = "error.etl.attachedFilesUpload";
    public static final String ETL_MALFORMED_URL = "error.etl.urlMalformed";
    public static final String ETL_URL_NOT_EXIST = "error.etl.urlNotExist";
    public static final String ETL_CLONE_REPOSITORY = "error.etl.cloneRepository";
    public static final String ETL_REPLACE_REPOSITORY = "error.etl.replacingRepository";
    public static final String ETL_TYPE_NOT_SUPPORTED = "error.etl.typeNotSupported";
    public static final String ETL_STATISTICAL_OPERATION_IS_BLANK = "error.etl.statisticalOperation.isBlank";
    public static final String ETL_HAS_THREADS_CONFIGURED = "error.etl.hasThreadsConfigured";
    public static final String ETL_UPDATE_EXISTS_RUNNING_ERROR = "error.etl.updateExistsRunning";
    public static final String ETL_DELETE_EXISTS_RUNNING_ERROR = "error.etl.deleteExistsRunning";
    public static final String ETL_STATISTICAL_HAS_THREADS_CONFIGURED = "error.etl.hasStatisticalThreadsConfigured";

    // HEALTH
    public static final String HEALTH_SERVICE_NAME_IS_BLANK = "error.health.serviceName.isBlank";
    public static final String HEALTH_ENDPOINT_IS_BLANK = "error.health.endpoint.isBlank";

    // PARAMETER
    public static final String PARAMETER_KEY_IS_DUPLICATED = "error.parameter.key.isDuplicated";
    public static final String PARAMETER_KEY_IS_BLANK = "error.parameter.key.isBlank";
    public static final String PARAMETER_VALUE_IS_BLANK = "error.parameter.value.isBlank";
    public static final String PARAMETER_EDIT = "error.parameter.edit";
    public static final String PARAMETER_KEY_IS_DUPLICATED_IN_GLOBAL_PARAMETER = "error.parameter.key.isDuplicatedInGlobalParameter";
    public static final String GLOBAL_PARAMETER_KEY_IS_DUPLICATED = "error.parameter.global.key.isDuplicated";

    // QUARZT
    public static final String QUARTZ_JOB_EXECUTION_ERROR = "error.quartz.jobExecutionError";

    // COMPUTATIONAL THREAD
    public static final String COMPUTATIONAL_THREAD_CRON_EXPRESSION_NOT_VALID = "error.computationalThread.cronExpressionNotValid";
    public static final String COMPUTATIONAL_THREAD_SCHEDULE_ERROR = "error.computationalThread.scheduleError";
    public static final String COMPUTATIONAL_THREAD_UNSCHEDULE_ERROR = "error.computationalThread.unscheduleError";
    public static final String COMPUTATIONAL_THREAD_CODE_EXISTS = "error.computationalThread.codeExists";
    public static final String COMPUTATIONAL_THREAD_STATISTICAL_OPERATION_IS_BLANK = "error.computationalThread.statisticalOperation.isBlank";
    public static final String COMPUTATIONAL_THREAD_CURRENTLY_DELETED = "error.computationalThread.currentlyDeleted";
    public static final String COMPUTATIONAL_THREAD_CURRENTLY_NOT_DELETED = "error.computationalThread.currentlyNotDeleted";
    public static final String COMPUTATIONAL_THREAD_FIELD_LIMIT_EXCEED_ERROR = "error.computationalThread.limitExceedError";
    public static final String COMPUTATIONAL_THREAD_NOT_ETL_CONFIGURED = "error.computationalThread.etlNotConfigured";

    // COMPUTATIONAL THREAD EXECUTION
    public static final String COMPUTATIONAL_THREAD_EXECUTION_REGISTER_ETL_ERROR = "error.computationalThread.registerEtlError";
    public static final String COMPUTATIONAL_THREAD_EXECUTION_ERROR = "error.computationalThread.executingError";

    private ErrorConstants() {
    }

}
