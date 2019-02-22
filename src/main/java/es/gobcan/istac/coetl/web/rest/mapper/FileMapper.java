package es.gobcan.istac.coetl.web.rest.mapper;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import es.gobcan.istac.coetl.domain.File;
import es.gobcan.istac.coetl.repository.FileRepository;
import es.gobcan.istac.coetl.web.rest.dto.FileDTO;

@Mapper(componentModel = "spring", uses = {})
public abstract class FileMapper implements EntityMapper<FileDTO, File> {

    @Autowired
    FileRepository fileRepository;

    public File fromId(Long id) {
        return fileRepository.findOne(id);
    }

    public File toEntity(FileDTO dto) {
        if (dto == null) {
            return null;
        }

        File entity = (dto.getId() != null) ? fromId(dto.getId()) : new File();

        entity.setDataContentType(dto.getDataContentType());
        entity.setLength(dto.getLength());
        entity.setName(dto.getName());

        return entity;
    }
}
