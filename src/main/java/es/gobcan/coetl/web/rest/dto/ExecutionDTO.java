package es.gobcan.coetl.web.rest.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import es.gobcan.coetl.domain.Execution.Result;
import es.gobcan.coetl.domain.Execution.Type;

public class ExecutionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Instant planningDate;
    private Instant startDate;
    private Instant finishDate;
    private Type type;
    private Result result;
    private String notes;

    private Long idEtl;

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

    public Long getIdEtl() {
        return idEtl;
    }

    public void setIdEtl(Long idEtl) {
        this.idEtl = idEtl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ExecutionDTO execution = (ExecutionDTO) o;
        return !(execution.getId() == null || getId() == null) && Objects.equals(getId(), execution.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        //@formatter:off
        return "Execution (" +
                    "id = " + getId() + 
                    ", dateTime = " + getPlanningDate() + 
                    ", type = " + getType() + 
                    ", result = " + getResult() + 
                    ", notes = " + getNotes() + 
                ")";
        //@formatter:on
    }
}
