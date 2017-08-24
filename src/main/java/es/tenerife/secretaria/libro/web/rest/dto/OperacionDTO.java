package es.tenerife.secretaria.libro.web.rest.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Operacion entity.
 */
public class OperacionDTO implements Serializable {

    private Long id;

    @NotNull
    private String accion;

    @NotNull
    private String sujeto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getSujeto() {
        return sujeto;
    }

    public void setSujeto(String sujeto) {
        this.sujeto = sujeto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OperacionDTO operacionDTO = (OperacionDTO) o;
        if(operacionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), operacionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OperacionDTO{" +
            "id=" + getId() +
            ", accion='" + getAccion() + "'" +
            ", sujeto='" + getSujeto() + "'" +
            "}";
    }
}
