package es.gobcan.istac.coetl.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "tb_healths")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Health implements Serializable {

    private static final long serialVersionUID = -374468324690945038L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "health_id_seq")
    @SequenceGenerator(name = "health_id_seq", sequenceName = "health_id_seq", initialValue = 10)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(name = "service_name", nullable = false, unique = true, length = 255)
    private String serviceName;

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(name = "endpoint", nullable = false, unique = true, length = 255)
    private String endpoint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}