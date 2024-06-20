package es.gobcan.istac.coetl.domain;

import java.io.Serializable;
import java.time.Instant;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.collection.internal.PersistentList;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "computational_threads_executions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ComputationalThreadExecution implements Serializable {

    private static final long serialVersionUID = -3898759537343270416L;

    public enum Type {
        AUTO, MANUAL
    }

    public enum Result {
        SUCCESS, FAILED, RUNNING, WAITING, DUPLICATED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "computational_threads_execution_id_seq")
    @SequenceGenerator(name = "computational_threads_execution_id_seq", sequenceName = "computational_threads_execution_id_seq", initialValue = 10)
    private Long id;

    @NotNull
    @Column(name = "planning_date", nullable = false)
    private Instant planningDate;

    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "finish_date", nullable = true)
    private Instant finishDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "result", nullable = false)
    private Result result;

    @Size(max = 4000)
    @Column(name = "notes", length = 4000, nullable = true)
    private String notes;

    @ManyToOne(optional = false, targetEntity = ComputationalThreads.class)
    @JoinColumn(name = "computational_thread_fk")
    private ComputationalThreads computationalThread;

    @Column(name = "executor", length = 250, nullable = true)
    private String executor;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = ComputationalThreadExecutionEtl.class, mappedBy = "computationalThreadExecution",
            cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    @OrderBy("id ASC")
    private List<ComputationalThreadExecutionEtl> computationalThreadExecutionEtl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getPlanningDate() {
        return planningDate;
    }

    public void setPlanningDate(Instant planningDate) {
        this.planningDate = planningDate;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Instant finishDate) {
        this.finishDate = finishDate;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ComputationalThreads getComputationalThread() {
        return computationalThread;
    }

    public void setComputationalThread(ComputationalThreads computationalThread) {
        this.computationalThread = computationalThread;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public List<ComputationalThreadExecutionEtl> getComputationalThreadExecutionEtl() {
        return computationalThreadExecutionEtl;
    }

    public void setComputationalThreadExecutionEtl(List<ComputationalThreadExecutionEtl> computationalThreadExecutionEtl) {
        if (computationalThreadExecutionEtl instanceof PersistentList) {
            this.computationalThreadExecutionEtl = computationalThreadExecutionEtl;
        } else {
            if (this.computationalThreadExecutionEtl == null) {
                this.computationalThreadExecutionEtl = computationalThreadExecutionEtl;
            }
            this.computationalThreadExecutionEtl.clear();
            this.computationalThreadExecutionEtl.addAll(computationalThreadExecutionEtl);
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

        ComputationalThreadExecution computationalThreadExecution = (ComputationalThreadExecution) o;
        return !(computationalThreadExecution.getId() == null || getId() == null) && Objects.equals(getId(), computationalThreadExecution.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        //@formatter:off
        return "ComputationalThreadExecution (" +
                    "id = " + getId() +
                    ", planningDate = " + getPlanningDate() + 
                    ", type = " + getType() + 
                    ", result = " + getResult() + 
                    ", notes = " + getNotes() + 
                ")";
        //@formatter:on
    }

}
