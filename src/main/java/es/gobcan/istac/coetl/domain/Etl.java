package es.gobcan.istac.coetl.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "tb_etls")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Etl extends AbstractVersionedAndAuditingWithDeletionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Type {
        TRANSFORMATION, JOB
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "etl_id_seq")
    @SequenceGenerator(name = "etl_id_seq", sequenceName = "etl_id_seq", initialValue = 10)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(name = "code", nullable = false, unique = true, length = 255)
    private String code;

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Size(max = 4000)
    @Column(name = "purpose", length = 4000)
    private String purpose;

    @NotBlank
    @Size(min = 1, max = 4000)
    @Column(name = "organization_in_charge", nullable = false, length = 4000)
    private String organizationInCharge;

    @NotBlank
    @Size(min = 1, max = 4000)
    @Column(name = "functional_in_charge", nullable = false, length = 4000)
    private String functionalInCharge;

    @NotBlank
    @Size(min = 1, max = 4000)
    @Column(name = "technical_in_charge", nullable = false, length = 4000)
    private String technicalInCharge;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 255)
    private Type type;

    @Size(max = 4000)
    @Column(name = "comments", length = 4000)
    private String comments;

    @Size(max = 4000)
    @Column(name = "execution_description", length = 4000)
    private String executionDescription;

    @Size(max = 255)
    @Column(name = "execution_planning", length = 255)
    private String executionPlanning;

    @Column(name = "next_execution")
    private Instant nextExecution;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "etl_description_file_fk")
    private File etlDescriptionFile;
    
    @NotNull
    @Size(max = 255)
    @Column(name = "uri_repository", length = 255)
    private String uriRepository;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getOrganizationInCharge() {
        return organizationInCharge;
    }

    public void setOrganizationInCharge(String organizationInCharge) {
        this.organizationInCharge = organizationInCharge;
    }

    public String getFunctionalInCharge() {
        return functionalInCharge;
    }

    public void setFunctionalInCharge(String functionalInCharge) {
        this.functionalInCharge = functionalInCharge;
    }

    public String getTechnicalInCharge() {
        return technicalInCharge;
    }

    public void setTechnicalInCharge(String technicalInCharge) {
        this.technicalInCharge = technicalInCharge;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isJob() {
        return Type.JOB.equals(type);
    }

    public boolean isTransformation() {
        return Type.TRANSFORMATION.equals(type);
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getExecutionDescription() {
        return executionDescription;
    }

    public void setExecutionDescription(String executionDescription) {
        this.executionDescription = executionDescription;
    }

    public String getExecutionPlanning() {
        return executionPlanning;
    }

    public void setExecutionPlanning(String executionPlanning) {
        this.executionPlanning = executionPlanning;
    }

    public boolean isPlanned() {
        return StringUtils.isNotBlank(executionPlanning);
    }

    public Instant getNextExecution() {
        return nextExecution;
    }

    public void setNextExecution(Instant nextExecution) {
        this.nextExecution = nextExecution;
    }

    public File getEtlDescriptionFile() {
        return etlDescriptionFile;
    }

    public void setEtlDescriptionFile(File etlDescriptionFile) {
        this.etlDescriptionFile = etlDescriptionFile;
    }
    
    public String getUriRepository() {
        return uriRepository;
    }

    public void setUriRepository(String uriRepository) {
        this.uriRepository = uriRepository;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Etl etl = (Etl) o;
        return !(etl.getId() == null || getId() == null) && Objects.equals(getId(), etl.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        //@formatter:off
        return "Etl (" +
                    "id = " + getId() + 
                    ", code = " + getCode() + 
                    ", name = " + getName() + 
                    ", type = " + getType() + 
                ")";
        //@formatter:on
    }
}
