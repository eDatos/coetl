package es.tenerife.secretaria.libro.web.rest.mapper;

import es.tenerife.secretaria.libro.domain.*;
import es.tenerife.secretaria.libro.web.rest.dto.OperacionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Operacion and its DTO OperacionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OperacionMapper extends EntityMapper <OperacionDTO, Operacion> {
    
    
    default Operacion fromId(Long id) {
        if (id == null) {
            return null;
        }
        Operacion operacion = new Operacion();
        operacion.setId(id);
        return operacion;
    }
}
