package com.arte.application.template.web.rest.errors;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_ACCESS_DENIED = "error.accessDenied";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String ERR_METHOD_NOT_SUPPORTED = "error.methodNotSupported";
    public static final String ERR_INTERNAL_SERVER_ERROR = "error.internalServerError";

    public static final String USER_EXISTS = "error.usuario-existe";
    public static final String USER_LDAP_NOT_FOUND = "error.userManagement.usuario-ldap-no-encontrado";
    public static final String USER_NOT_VALID = "error.userManagement.usuario-no-valido";

    public static final String ROLE_NEED_OPERATIONS = "error.rol.validation.rol-necesita-operaciones";

    public static final String FILE_EMPTY = "error.file-empty";
    public static final String FILE_NOT_FOUND = "error.file-not-found";

    public static final String ENTITY_EXISTS = "error.entidad-existe";
    public static final String ENTITY_NOT_FOUND = "error.entidad-no-encontrada";
    public static final String ID_EXISTS = "error.id-existe";
    public static final String ID_MISSING = "error.id-falta";
    public static final String CODE_MISSING = "error.codigo-falta";
    public static final String EMAIL_EXISTS = "error.email-existe";

    public static final String QUERY_NOT_SUPPORTED = "error.query-no-soportada";

    private ErrorConstants() {
    }

}
