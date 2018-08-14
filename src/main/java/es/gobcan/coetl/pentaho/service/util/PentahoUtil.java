package es.gobcan.coetl.pentaho.service.util;

import java.nio.charset.Charset;

import org.apache.commons.lang3.CharEncoding;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import es.gobcan.coetl.config.PentahoProperties;
import es.gobcan.coetl.pentaho.dto.PentahoResponseDTO;
import es.gobcan.coetl.pentaho.enumeration.CarteMethodsEnum;

public final class PentahoUtil {

    private PentahoUtil() {
    }

    public static <E extends Enum<E> & CarteMethodsEnum, T extends PentahoResponseDTO> ResponseEntity<T> execute(String user, String password, String url, E pentahoMethod, HttpMethod httpMethod,
            MultiValueMap<String, String> queryParams, Class<T> clazz) {
        String uri = new StringBuilder().append(url).append(pentahoMethod.getResource()).toString();
        String uriWithQueryParameters = UriComponentsBuilder.fromHttpUrl(uri).queryParams(queryParams).toUriString();
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(uriWithQueryParameters, httpMethod, new HttpEntity<>(createHeaders(user, password)), clazz);
    }

    public static String getUrl(PentahoProperties pentahoProperties) {
        //@formatter:off
        return new StringBuilder()
                .append("http://")
                .append(pentahoProperties.getEndpoint().endsWith("/") ? pentahoProperties.getEndpoint() : pentahoProperties.getEndpoint() + "/")
                .append("kettle/")
                .toString();
        //@formatter:on
    }

    public static String getUser(PentahoProperties pentahoProperties) {
        return pentahoProperties.getAuth().getUser();
    }

    public static String getPassword(PentahoProperties pentahoProperties) {
        return pentahoProperties.getAuth().getPassword();
    }

    private static HttpHeaders createHeaders(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName(CharEncoding.UTF_8)));
        String authHeader = "Basic " + new String(encodedAuth);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        headers.setContentType(MediaType.APPLICATION_XML);
        return headers;
    }
}
