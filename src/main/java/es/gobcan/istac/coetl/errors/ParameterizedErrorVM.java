package es.gobcan.istac.coetl.errors;

import java.io.Serializable;
import java.util.List;

public class ParameterizedErrorVM implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String code;
    private final String message;
    private final List<String> params;
    private final List<ParameterizedErrorItem> errorItems;

    public ParameterizedErrorVM(String message, String code, List<String> params, List<ParameterizedErrorItem> errorItems) {
        this.code = code;
        this.message = message;
        this.params = params;
        this.errorItems = errorItems;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getParams() {
        return params;
    }

    public List<ParameterizedErrorItem> getErrorItems() {
        return errorItems;
    }
}
