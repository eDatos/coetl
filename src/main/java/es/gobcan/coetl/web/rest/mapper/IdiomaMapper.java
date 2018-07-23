package es.gobcan.coetl.web.rest.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.coetl.domain.Idioma;
import es.gobcan.coetl.repository.IdiomaRepository;
import es.gobcan.coetl.web.rest.dto.IdiomaDTO;

/**
 * Mapper for the entity Idioma and its DTO IdiomaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class IdiomaMapper implements EntityMapper<IdiomaDTO, Idioma> {

    @Autowired
    private IdiomaRepository idiomaRepository;

    public Idioma fromId(Long id) {
        if (id == null) {
            return null;
        }
        return idiomaRepository.findOne(id);
    }
}
