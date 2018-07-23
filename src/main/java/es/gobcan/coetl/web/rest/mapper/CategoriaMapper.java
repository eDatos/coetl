package es.gobcan.coetl.web.rest.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.coetl.domain.Categoria;
import es.gobcan.coetl.repository.CategoriaRepository;
import es.gobcan.coetl.web.rest.dto.CategoriaDTO;

/**
 * Mapper for the entity Categoria and its DTO CategoriaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class CategoriaMapper implements EntityMapper<CategoriaDTO, Categoria> {

    @Autowired
    private CategoriaRepository categoriaRepository;

    abstract Set<Categoria> toEntity(Set<CategoriaDTO> categoriasDTO);

    abstract Set<CategoriaDTO> toDto(Set<Categoria> categorias);

    public Categoria fromId(Long id) {
        if (id == null) {
            return null;
        }
        return categoriaRepository.findOne(id);
    }

    public Categoria toEntity(CategoriaDTO dto) {
        if (dto == null) {
            return null;
        }

        Categoria categoria = new Categoria();

        if (dto.getId() != null) {
            categoria = fromId(dto.getId());
        }

        categoria.setNombre(dto.getNombre());

        return categoria;
    }
}
