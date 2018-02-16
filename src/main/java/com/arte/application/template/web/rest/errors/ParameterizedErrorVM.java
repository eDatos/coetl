package com.arte.application.template.web.rest.errors;

import java.io.Serializable;
import java.util.List;

public class ParameterizedErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String code;
    private final String message;
    private final List<String> paramList;
    private final List<ParameterizedErrorItem> errorItems;

    public ParameterizedErrorVM(String message, String code, List<String> paramList, List<ParameterizedErrorItem> errorItems) {
        this.code = code;
        this.message = message;
        this.paramList = paramList;
        this.errorItems = errorItems;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getParamList() {
        return paramList;
    }

    public List<ParameterizedErrorItem> getErrorItems() {
        return errorItems;
    }

}
