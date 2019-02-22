package es.gobcan.istac.coetl.pentaho.enumeration;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum(String.class)
public enum Status {
    //@formatter:off
    @XmlEnumValue("Running") RUNNING,
    @XmlEnumValue("Stopped") STOPPED,
    @XmlEnumValue("Waiting") WAITING,
    @XmlEnumValue("Finished") FINISHED,
    @XmlEnumValue("Finished (with errors)") FINISHED_WITH_ERRORS
    //@formatter:on
}
