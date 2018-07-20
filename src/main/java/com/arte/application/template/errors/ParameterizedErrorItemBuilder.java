package com.arte.application.template.errors;

public class ParameterizedErrorItemBuilder {

    private String message;

    private String code;
    private String[] params;

    public ParameterizedErrorItemBuilder code(String code) {
        this.code = code;
        return this;
    }

    public ParameterizedErrorItemBuilder code(String code, String... params) {
        this.code = code;
        this.params = params;
        return this;
    }

    public ParameterizedErrorItemBuilder message(String message) {
        this.message = message;
        return this;
    }

    public ParameterizedErrorItem build() {
        return new ParameterizedErrorItem(message, code, params);
    }
}
