package es.gobcan.coetl.pentaho.web.rest.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "webresult")
public class WebResultDTO implements PentahoResponseDTO {

    @XmlEnum(String.class)
    public enum Result {
        //@formatter:off
        @XmlEnumValue("OK") OK, 
        @XmlEnumValue("ERROR") ERROR
        //@formatter:on
    }

    private static final long serialVersionUID = 1L;

    private Result result;

    private String message;

    private String id;

    @XmlElement(name = "result")
    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @XmlElement(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isOk() {
        return Result.OK.equals(result);
    }
}
