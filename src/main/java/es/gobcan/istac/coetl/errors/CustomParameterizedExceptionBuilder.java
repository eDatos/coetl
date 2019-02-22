package es.gobcan.istac.coetl.errors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomParameterizedExceptionBuilder {

    private String message;

    private String code;
    private List<String> params = new ArrayList<>();

    private Throwable cause;

    private List<ParameterizedErrorItem> errorItems = new ArrayList<>();

    public CustomParameterizedExceptionBuilder code(String code) {
        this.code = code;
        return this;
    }

    public CustomParameterizedExceptionBuilder code(String code, String... params) {
        this.code = code;
        if (params != null) {
            this.params = Arrays.asList(params);
        }
        return this;
    }

    public CustomParameterizedExceptionBuilder message(String message) {
        this.message = message;
        return this;
    }

    public CustomParameterizedExceptionBuilder cause(Throwable cause) {
        this.cause = cause;
        return this;
    }

    public CustomParameterizedExceptionBuilder errorItem(ParameterizedErrorItem errorItem) {
        this.errorItems.add(errorItem);
        return this;
    }

    public CustomParameterizedExceptionBuilder errorItems(List<ParameterizedErrorItem> errorItems) {
        this.errorItems.addAll(errorItems);
        return this;
    }

    public CustomParameterizedException build() {
        CustomParameterizedException exception;
        if (cause == null) {
            exception = new CustomParameterizedException(message);
        } else {
            exception = new CustomParameterizedException(message, cause);
        }
        exception.setCode(code);
        exception.setParams(params);
        for (ParameterizedErrorItem item : errorItems) {
            exception.addErrorItem(item);
        }
        return exception;
    }
}