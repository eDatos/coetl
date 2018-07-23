package es.gobcan.coetl.web.rest.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.coetl.domain.Documento;
import es.gobcan.coetl.repository.DocumentoRepository;
import es.gobcan.coetl.web.rest.dto.DocumentoDTO;

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
