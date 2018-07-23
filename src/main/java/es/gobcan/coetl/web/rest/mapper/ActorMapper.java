package es.gobcan.coetl.web.rest.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.coetl.domain.Actor;
import es.gobcan.coetl.repository.ActorRepository;
import es.gobcan.coetl.web.rest.dto.ActorDTO;

/**
 * Mapper for the entity Actor and its DTO ActorDTO.
 */
@Mapper(componentModel = "spring", uses = {DocumentoMapper.class})
public abstract class ActorMapper implements EntityMapper<ActorDTO, Actor> {

    @Autowired
    private ActorRepository actorRepository;

    abstract Set<Actor> toEntity(Set<ActorDTO> actoresDTO);

    abstract Set<ActorDTO> toDto(Set<Actor> actores);

    public Actor fromId(Long id) {
        if (id == null) {
            return null;
        }
        return actorRepository.findOne(id);
    }
}
