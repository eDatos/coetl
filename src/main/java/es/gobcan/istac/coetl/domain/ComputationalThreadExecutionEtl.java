package es.gobcan.istac.coetl.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "tb_threads_execution_etls")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ComputationalThreadExecutionEtl implements Serializable {

    private static final long serialVersionUID = -6228537202081963421L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "threads_execution_etls_id_seq")
    @SequenceGenerator(name = "threads_execution_etls_id_seq", sequenceName = "threads_execution_etls_id_seq", initialValue = 10)
    private Long id;

    @ManyToOne(optional = false, targetEntity = ComputationalThreadExecution.class)
    @JoinColumn(name = "id_computational_thread_execution")
    private ComputationalThreadExecution computationalThreadExecution;

    @ManyToOne(fetch= FetchType.EAGER, optional = false, targetEntity = Execution.class)
    @JoinColumn(name = "id_etl_execution")
    private Execution execution;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ComputationalThreadExecution getComputationalThreadExecution() {
        return computationalThreadExecution;
    }

    public void setComputationalThreadExecution(ComputationalThreadExecution computationalThreadExecution) {
        this.computationalThreadExecution = computationalThreadExecution;
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
    }

    @PrePersist
    @PreUpdate
    public void updateEtlAssociation() {
        if (computationalThreadExecution != null && execution != null) {
            this.setComputationalThreadExecution(computationalThreadExecution);
            this.setExecution(execution);
        }
    }

}
