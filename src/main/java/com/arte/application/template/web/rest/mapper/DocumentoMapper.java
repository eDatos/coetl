package com.arte.application.template.web.rest.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.application.template.domain.Documento;
import com.arte.application.template.repository.DocumentoRepository;
import com.arte.application.template.web.rest.dto.DocumentoDTO;

@Mapper(componentModel = "spring", uses = {})
public abstract class DocumentoMapper implements EntityMapper<DocumentoDTO, Documento> {

    @Autowired
    DocumentoRepository documentoRepository;

    public Documento fromId(Long id) {
        if (id == null) {
            return null;
        }

        return documentoRepository.findOne(id);
    }
}
