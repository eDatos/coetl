package es.gobcan.istac.coetl.errors;

import java.util.ArrayList;
import java.util.List;

public class CustomParameterizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;
    private List<String> params = new ArrayList<>();
    private List<ParameterizedErrorItem> errorItems = new ArrayList<>();

    protected CustomParameterizedException(String message) {
        super(message);
    }

    protected CustomParameterizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterizedErrorVM getParameterizedErrorVM() {
        return new ParameterizedErrorVM(getMessage(), code, params, errorItems);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public void addErrorItem(ParameterizedErrorItem errorItem) {
        this.errorItems.add(errorItem);
    }
}
