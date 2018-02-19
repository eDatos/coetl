package com.arte.application.template.web.rest.errors;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Custom, parameterized exception, which can be translated on the client side.
 * For example:
 *
 * <pre>
 * throw new CustomParameterizedException(&quot;myCustomError&quot;, &quot;hello&quot;, &quot;world&quot;);
 * </pre>
 *
 * Can be translated with:
 *
 * <pre>
 * "error.myCustomError" :  "The server says {{param0}} to {{param1}}"
 * </pre>
 */
public class CustomParameterizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;
    private final String message;
    private final List<String> paramList;

    private final List<ParameterizedErrorItem> errorItems;

    public CustomParameterizedException(String message, Throwable e, String code, String... params) {
        super(String.format(message, (params != null && params.length > 0) ? params : null), e);
        this.code = code;
        this.message = message;
        this.paramList = (params == null) ? new LinkedList<>() : Arrays.asList(params);
        this.errorItems = null;
    }

    public CustomParameterizedException(String message, Throwable e, String code, List<ParameterizedErrorItem> errorItems, String... params) {
        super(String.format(message, (params != null && params.length > 0) ? params : null), e);
        this.code = code;
        this.message = message;
        this.paramList = (params == null) ? new LinkedList<>() : Arrays.asList(params);
        this.errorItems = errorItems;
    }

    public CustomParameterizedException(String message, String code, String... params) {
        super(String.format(message, (params != null && params.length > 0) ? params : null));
        this.code = code;
        this.message = message;
        this.paramList = (params == null) ? new LinkedList<>() : Arrays.asList(params);
        this.errorItems = null;
    }

    public CustomParameterizedException(String message, String code, List<ParameterizedErrorItem> errorItems, String... params) {
        super(String.format(message, (params != null && params.length > 0) ? params : null));
        this.code = code;
        this.message = message;
        this.paramList = (params == null) ? new LinkedList<>() : Arrays.asList(params);
        this.errorItems = errorItems;
    }

    public ParameterizedErrorVM getParameterizedErrorVM() {
        return new ParameterizedErrorVM(message, code, paramList, errorItems);
    }
}
