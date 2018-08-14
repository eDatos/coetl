package es.gobcan.coetl.web.rest.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractAuditingDTO {

    private Instant createdDate = Instant.now();

    private String createdBy;

    private String lastModifiedBy;

    private Instant lastModifiedDate = Instant.now();

    @JsonProperty
    public String getCreatedBy() {
        return createdBy;
    }

    @JsonIgnore
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @JsonProperty
    public Instant getCreatedDate() {
        return createdDate;
    }

    @JsonIgnore
    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    @JsonProperty
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    @JsonIgnore
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @JsonProperty
    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    @JsonIgnore
    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

}
