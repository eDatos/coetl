package es.gobcan.istac.coetl.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.google.common.base.CaseFormat;

public final class StringUtils {

    private StringUtils() {

    }

    public static String toLowerCamelCase(String upperCamelCase) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, upperCamelCase);
    }

    public static String changeFormatStringDate(String value){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());;
        String val = value.replace("'","");
        Instant date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(val).toInstant();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatter.format(date);
    }
}
