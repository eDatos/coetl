package es.tenerife.secretaria.libro.web.rest.errors;

public final class ErrorConstants {

	public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
	public static final String ERR_ACCESS_DENIED = "error.accessDenied";
	public static final String ERR_VALIDATION = "error.validation";
	public static final String ERR_METHOD_NOT_SUPPORTED = "error.methodNotSupported";
	public static final String ERR_INTERNAL_SERVER_ERROR = "error.internalServerError";

	public static final String ID_EXISTE = "id-existe";
	public static final String ID_FALTA = "id-falta";
	public static final String CODIGO_FALTA = "codigo-falta";
	public static final String ENTIDAD_EXISTE = "entidad-existe";

	public static final String ENTIDAD_NO_ENCONTRADA = "entidad-no-encontrada";

	private ErrorConstants() {
	}

}
