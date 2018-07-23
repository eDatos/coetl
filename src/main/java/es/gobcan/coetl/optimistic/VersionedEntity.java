package es.gobcan.coetl.optimistic;

import java.io.Serializable;

public interface VersionedEntity {

    Serializable getId();

    Long getOptLock();

    void setOptLock(Long optLock);

}