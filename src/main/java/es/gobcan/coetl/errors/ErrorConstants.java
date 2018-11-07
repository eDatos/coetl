package es.gobcan.coetl.errors;

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

    public static final String FICHERO_VACIO = "error.file.single.empty";
    public static final String FICHERO_NO_ENCONTRADO = "error.file-not-found";

    public static final String ENTIDAD_NO_ENCONTRADA = "error.entidad-no-encontrada";
    public static final String ID_EXISTE = "error.id-existe";
    public static final String ID_FALTA = "error.id-falta";

    public static final String QUERY_NO_SOPORTADA = "error.query-no-soportada";

    // ETL
    public static final String ETL_CURRENTLY_DELETED = "error.etl.currentlyDeleted";
    public static final String ETL_CURRENTLY_NOT_DELETED = "error.etl.currentlyNotDeleted";
    public static final String ETL_FILE_CURRENTLY_DELETED = "error.etl.etlFileCurrentlyDeleted";
    public static final String ETL_DESCRIPTION_FILE_CURRENTLY_DELETED = "error.etl.etlDescriptionFileCurrentlyDeleted";
    public static final String ETL_CRON_EXPRESSION_NOT_VALID = "error.etl.cronExpressionNotValid";
    public static final String ETL_SCHEDULE_ERROR = "error.etl.scheduleError";
    public static final String ETL_UNSCHEDULE_ERROR = "error.etl.unscheduleError";
    public static final String ETL_CODE_EXISTS = "error.etl.codeExists";

    // QUARZT
    public static final String QUARTZ_JOB_EXECUTION_ERROR = "error.quartz.jobExecutionError";

    private ErrorConstants() {
    }

}
