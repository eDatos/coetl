package es.tenerife.secretaria.libro.web.rest.mapper;

import java.util.List;

import org.mapstruct.MappingTarget;

/**
 * Contract for a generic dto to entity mapper.
 * 
 * @param <D>
 *            - DTO type parameter.
 * @param <E>
 *            - Entity type parameter.
 */

public interface EntityMapper<D, E> {

	public E toEntity(D dto);

	public D toDto(E entity);

	public E update(@MappingTarget E entity, D dto);

	public List<E> toEntity(List<D> dtoList);

	public List<D> toDto(List<E> entityList);
}
