package es.gobcan.istac.coetl.web.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import es.gobcan.istac.coetl.domain.ComputationalThreadsEtl;
import es.gobcan.istac.coetl.web.rest.dto.ComputationalThreadsEtlDTO;

@Mapper(componentModel = "spring", uses = {})
public interface ComputationalThreadsEtlMapper {

    ComputationalThreadsEtlMapper INSTANCIA= Mappers.getMapper(ComputationalThreadsEtlMapper.class);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "etl", target = "etl")
    @Mapping(source = "executionOrder", target = "executionOrder")
    ComputationalThreadsEtlDTO toDto(ComputationalThreadsEtl entity);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "etl", target = "etl")
    @Mapping(source = "executionOrder", target = "executionOrder")
    ComputationalThreadsEtl toEntity(ComputationalThreadsEtlDTO entity);

}
