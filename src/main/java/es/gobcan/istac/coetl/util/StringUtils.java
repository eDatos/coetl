package es.gobcan.istac.coetl.util;

import com.google.common.base.CaseFormat;

public final class StringUtils {

    private StringUtils() {

    }

    public static String toLowerCamelCase(String upperCamelCase) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, upperCamelCase);
    }
}
