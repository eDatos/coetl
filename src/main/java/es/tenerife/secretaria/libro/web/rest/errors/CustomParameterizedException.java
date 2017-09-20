package es.tenerife.secretaria.libro.web.rest.errors;

import java.util.HashMap;
import java.util.Map;

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

	private static final String PARAM = "param";

	private final String message;

	private final Map<String, String> paramMap = new HashMap<>();

	public CustomParameterizedException(String code, String... params) {
		super(code);
		this.message = code;
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				paramMap.put(PARAM + i, params[i]);
			}
		}
	}

	public CustomParameterizedException(String code, Map<String, String> paramMap) {
		super(code);
		this.message = code;
		this.paramMap.putAll(paramMap);
	}

	public ParameterizedErrorVM getErrorVM() {
		return new ParameterizedErrorVM(message, paramMap);
	}
}
