package es.gobcan.istac.coetl.web.rest.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import org.siemac.edatos.core.common.enume.TypeExternalArtefactsEnum;

public class StatisticalOperationDTO extends ExternalItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean enabled;
    private Date publicationDate;
    private TypeExternalArtefactsEnum type;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public TypeExternalArtefactsEnum getType() {
        return type;
    }

    public void setType(TypeExternalArtefactsEnum type) {
        this.type = TypeExternalArtefactsEnum.STATISTICAL_OPERATION;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StatisticalOperationDTO etlDTO = (StatisticalOperationDTO) o;
        return !(etlDTO.getId() == null || getId() == null) && Objects.equals(getId(), etlDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }


}
