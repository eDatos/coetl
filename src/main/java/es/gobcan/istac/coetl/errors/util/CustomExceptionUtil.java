package es.gobcan.istac.coetl.errors.util;

import es.gobcan.istac.coetl.errors.CustomParameterizedExceptionBuilder;

public final class CustomExceptionUtil {

    private CustomExceptionUtil() {
    }

    public static void throwCustomParameterizedException(String errorMessage, Throwable cause, String errorConstant, String... params) {
        //@formatter:off
        throw new CustomParameterizedExceptionBuilder()
            .message(errorMessage)
            .cause(cause)
            .code(errorConstant, params)
            .build();
        //@formatter:on
    }

    public static void throwCustomParameterizedException(String errorMessage, String errorConstant, String... params) {
        //@formatter:off
        throw new CustomParameterizedExceptionBuilder()
        .message(errorMessage)
        .code(errorConstant, params)
        .build();
        //@formatter:on
    }
}
