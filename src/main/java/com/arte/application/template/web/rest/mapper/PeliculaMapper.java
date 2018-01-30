package com.arte.application.template.web.rest.mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.application.template.domain.Pelicula;
import com.arte.application.template.repository.PeliculaRepository;
import com.arte.application.template.web.rest.dto.PeliculaDTO;

/**
 * Mapper for the entity Pelicula and its DTO PeliculaDTO.
 */
@Mapper(componentModel = "spring", uses = {ActorMapper.class, CategoriaMapper.class, IdiomaMapper.class, DocumentoMapper.class})
public abstract class PeliculaMapper implements EntityMapper<PeliculaDTO, Pelicula> {

    @Autowired
    private PeliculaRepository peliculaRepository;

    @Autowired
    private ActorMapper actorMapper;

    @Autowired
    private CategoriaMapper categoriaMapper;

    @Autowired
    private IdiomaMapper idiomaMapper;

    @Autowired
    private DocumentoMapper documentoMapper;

    public Pelicula fromId(Long id) {
        if (id == null) {
            return null;
        }
        return peliculaRepository.findOne(id);
    }

    public List<Pelicula> toEntity(List<PeliculaDTO> dtoList) {
        if (dtoList == null) {
            return Collections.emptyList();
        }

        List<Pelicula> list = new ArrayList<>();
        dtoList.stream().forEach(e -> list.add(toEntity(e)));

        return list;
    }

    public List<PeliculaDTO> toDto(List<Pelicula> entityList) {
        if (entityList == null) {
            return Collections.emptyList();
        }

        List<PeliculaDTO> list = new ArrayList<>();
        entityList.stream().forEach(e -> list.add(toDto(e)));

        return list;
    }

    public Set<Pelicula> toEntity(Set<PeliculaDTO> dtoSet) {
        if (dtoSet == null) {
            return Collections.emptySet();
        }

        Set<Pelicula> set = new HashSet<>();
        dtoSet.stream().forEach(e -> set.add(toEntity(e)));

        return set;
    }

    public Pelicula toEntity(PeliculaDTO dto) {
        if (dto == null) {
            return null;
        }

        Pelicula entity = new Pelicula();

        entity.setId(dto.getId());
        entity.setTitulo(dto.getTitulo());
        entity.setDescripcion(dto.getDescripcion());
        entity.setFechaEstreno(dto.getFechaEstreno());
        entity.setIdioma(idiomaMapper.toEntity(dto.getIdioma()));
        entity.setAllActores(actorMapper.toEntity(dto.getActores()));
        entity.setAllCategorias(categoriaMapper.toEntity(dto.getCategorias()));
        entity.setDocumento(documentoMapper.toEntity(dto.getDocumento()));
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setLastModifiedBy(dto.getLastModifiedBy());
        entity.setLastModifiedDate(dto.getLastModifiedDate());
        entity.setOptLock(dto.getOptLock());

        return entity;
    }

    public PeliculaDTO toDto(Pelicula entity) {
        if (entity == null) {
            return null;
        }

        PeliculaDTO dto = new PeliculaDTO();

        dto.setId(entity.getId());
        dto.setTitulo(entity.getTitulo());
        dto.setDescripcion(entity.getDescripcion());
        dto.setFechaEstreno(entity.getFechaEstreno());
        dto.setIdioma(idiomaMapper.toDto(entity.getIdioma()));
        dto.setActores(actorMapper.toDto(entity.getActores()));
        dto.setCategorias(categoriaMapper.toDto(entity.getCategorias()));
        dto.setDocumento(documentoMapper.toDto(entity.getDocumento()));
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setLastModifiedBy(entity.getLastModifiedBy());
        dto.setLastModifiedDate(entity.getLastModifiedDate());
        dto.setOptLock(entity.getOptLock());

        return dto;
    }
}
