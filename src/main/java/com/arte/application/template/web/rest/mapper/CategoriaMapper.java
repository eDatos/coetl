package com.arte.application.template.web.rest.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.application.template.domain.Categoria;
import com.arte.application.template.repository.CategoriaRepository;
import com.arte.application.template.web.rest.dto.CategoriaDTO;

/**
 * Mapper for the entity Categoria and its DTO CategoriaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public abstract class CategoriaMapper implements EntityMapper<CategoriaDTO, Categoria> {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria fromId(Long id) {
        if (id == null) {
            return null;
        }
        return categoriaRepository.findOne(id);
    }
}
