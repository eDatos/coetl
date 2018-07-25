package es.gobcan.coetl.domain;

import java.io.Serializable;

public interface VersionedEntity {

    Serializable getId();

    Long getOptLock();

    void setOptLock(Long optLock);

}