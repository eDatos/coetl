package es.gobcan.istac.coetl.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.collection.internal.PersistentList;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tb_threads")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ComputationalThreads extends AbstractVersionedAndAuditingWithDeletionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "threads_id_seq")
    @SequenceGenerator(name = "threads_id_seq", sequenceName = "threads_id_seq", initialValue = 10)
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
    @Column(name = "description", length = 4000)
    private String description;

    @Size(max = 4000)
    @Column(name = "execution_description", length = 4000)
    private String executionDescription;

    @Size(max = 255)
    @Column(name = "execution_planning", length = 255)
    private String executionPlanning;

    @Column(name = "next_execution")
    private Instant nextExecution;

    @ManyToOne(targetEntity = ExternalItem.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "external_item_fk")
    private ExternalItem externalItem;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = ComputationalThreadsEtl.class, mappedBy = "computationalThread",
            cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    @OrderBy("executionOrder ASC")
    private List<ComputationalThreadsEtl> computationalThreadsEtl;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ExternalItem getExternalItem() {
        return externalItem;
    }

    public void setExternalItem(ExternalItem externalItem) {
        this.externalItem = externalItem;
    }

    public List<ComputationalThreadsEtl> getComputationalThreadsEtl() {
        return computationalThreadsEtl;
    }

    public void setComputationalThreadsEtl(List<ComputationalThreadsEtl> computationalThreadsEtl) {
        if (computationalThreadsEtl instanceof PersistentList) {
            this.computationalThreadsEtl = computationalThreadsEtl;
        } else {
            if (this.computationalThreadsEtl == null) {
                this.computationalThreadsEtl = computationalThreadsEtl;
            }
            this.computationalThreadsEtl.clear();
            this.computationalThreadsEtl.addAll(computationalThreadsEtl);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ComputationalThreads computationalThread = (ComputationalThreads) o;
        return !(computationalThread.getId() == null || getId() == null) && Objects.equals(getId(), computationalThread.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        //@formatter:off
        return "ComputationalThreads (" +
                    "id = " + getId() +
                    ", code = " + getCode() +
                    ", name = " + getName() +
                ")";
        //@formatter:on
    }
}
