package es.gobcan.istac.coetl.domain;

import java.io.Serializable;

import javax.persistence.Column;
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
@Table(name = "computational_threads_etls")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ComputationalThreadsEtl implements Serializable {

    private static final long serialVersionUID = -3681983017374260686L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "computational_threads_etls_id_seq")
    @SequenceGenerator(name = "computational_threads_etls_id_seq", sequenceName = "computational_threads_etls_id_seq", initialValue = 10)
    private Long id;

    @ManyToOne(optional = false, targetEntity = ComputationalThreads.class)
    @JoinColumn(name = "id_computational_thread")
    private ComputationalThreads computationalThread;

    @ManyToOne(fetch= FetchType.EAGER, optional = false, targetEntity = Etl.class)
    @JoinColumn(name = "id_etl")
    private Etl etl;

    @Column(name = "execution_order", nullable = false)
    private Long executionOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ComputationalThreads getComputationalThread() {
        return computationalThread;
    }

    public void setComputationalThread(ComputationalThreads computationalThread) {
        this.computationalThread = computationalThread;
    }

    public Etl getEtl() {
        return etl;
    }

    public void setEtl(Etl etl) {
        this.etl = etl;
    }

    public Long getExecutionOrder() {
        return executionOrder;
    }

    public void setExecutionOrder(Long order) {
        this.executionOrder = order;
    }

    @PrePersist
    @PreUpdate
    public void updateEtlAssociation() {
        if (computationalThread != null && etl != null) {
            this.setComputationalThread(computationalThread);
            this.setEtl(etl);
        }
    }

}
