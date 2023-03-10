package es.gobcan.istac.coetl.platform.web.rest.converter;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.springframework.http.MediaType;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;

public class CustomJaxb2RootElementHttpMessageConverter extends Jaxb2RootElementHttpMessageConverter {

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return (clazz.isAnnotationPresent(XmlRootElement.class) || clazz.isAnnotationPresent(XmlType.class)) &&
        (super.canRead(mediaType) || customAceptedMediaType().contains(mediaType));
    }
    
    private List<MediaType> customAceptedMediaType() {
        MediaType workarround = new MediaType("text", "html", StandardCharsets.UTF_8);
        MediaType[] array = {MediaType.TEXT_HTML, MediaType.APPLICATION_XHTML_XML, workarround};
        return Arrays.asList(array);
    }
}
