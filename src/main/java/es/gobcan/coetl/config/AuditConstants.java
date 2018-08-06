package es.gobcan.coetl.config;

public final class AuditConstants {

    // Audits fields
    public static final String CODE = "code";
    public static final String MESSAGE = "message";

    // Audits types
    public static final String ROL_CREACION = "ROL_CREACION";
    public static final String ROL_BORRADO = "ROL_BORRADO";
    public static final String ROL_EDICION = "ROL_EDICION";

    public static final String USUARIO_CREACION = "USUARIO_CREACION";
    public static final String USUARIO_EDICION = "USUARIO_EDICION";
    public static final String USUARIO_DESACTIVACION = "USUARIO_DESACTIVACION";
    public static final String USUARIO_ACTIVACION = "USUARIO_ACTIVACION";

    // Audits ETL Type
    public static final String ETL_CREATED = "ETL_CREATED";
    public static final String ETL_UPDATED = "ETL_UPDATED";
    public static final String ETL_DELETED = "ETL_DELETED";
    public static final String ETL_RECOVERED = "ETL_RECOVERED";
    public static final String ETL_EXECUTED = "ETL_EXECUTED";

    private AuditConstants() {
    }
}
