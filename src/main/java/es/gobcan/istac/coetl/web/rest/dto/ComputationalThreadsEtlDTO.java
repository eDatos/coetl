package es.gobcan.istac.coetl.web.rest.dto;

import java.io.Serializable;

import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.domain.Etl;

public class ComputationalThreadsEtlDTO implements Serializable {

    private static final long serialVersionUID = 2699211247543833580L;

    private Long id;
    private ComputationalThreads computationalThread;
    private Etl etl;
    private Long executionOrder;

    public ComputationalThreadsEtlDTO() {
        super();
    }

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

    public void setExecutionOrder(Long executionOrder) {
        this.executionOrder = executionOrder;
    }

}
