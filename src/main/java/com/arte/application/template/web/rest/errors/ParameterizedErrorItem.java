package com.arte.application.template.web.rest.errors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ParameterizedErrorItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String code;
    private final String message;
    private final List<String> paramList;

    public ParameterizedErrorItem(String message, String code, String... params) {
        this.code = code;
        this.message = message;
        this.paramList = (params == null) ? new LinkedList<>() : Arrays.asList(params);
    }

    public ParameterizedErrorItem(String message, String code, List<String> paramList) {
        this.code = code;
        this.message = message;
        this.paramList = paramList;
    }

    public String getcode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getParamList() {
        return paramList;
    }
}
