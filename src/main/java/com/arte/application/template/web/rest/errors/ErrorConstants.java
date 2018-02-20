package com.arte.application.template.web.rest.errors;

public final class ErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_ACCESS_DENIED = "error.accessDenied";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String ERR_METHOD_NOT_SUPPORTED = "error.methodNotSupported";
    public static final String ERR_INTERNAL_SERVER_ERROR = "error.internalServerError";

    public static final String USUARIO_EXISTE = "error.usuario-existe";
    public static final String USUARIO_LDAP_NO_ENCONTRADO = "error.userManagement.usuario-ldap-no-encontrado";
    public static final String USUARIO_NO_VALIDO = "error.userManagement.usuario-no-valido";

    public static final String ROL_NECESITA_OPERACIONES = "error.rol.validation.rol-necesita-operaciones";
    public static final String ROL_NO_ENCONTRADO = "error.rol-no-encontrado";

    public static final String FICHERO_VACIO = "error.file-empty";
    public static final String FICHERO_NO_ENCONTRADO = "error.file-not-found";

    public static final String ENTIDAD_EXISTE = "error.entidad-existe";
    public static final String ENTIDAD_NO_ENCONTRADA = "error.entidad-no-encontrada";
    public static final String ID_EXISTE = "error.id-existe";
    public static final String ID_FALTA = "error.id-falta";
    public static final String CODIGO_FALTA = "error.codigo-falta";
    public static final String EMAIL_EXISTE = "error.email-existe";

    public static final String QUERY_NO_SOPORTADA = "error.query-no-soportada";

    private ErrorConstants() {
    }

}
