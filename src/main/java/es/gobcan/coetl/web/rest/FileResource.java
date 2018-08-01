package es.gobcan.coetl.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import es.gobcan.coetl.domain.File;
import es.gobcan.coetl.errors.ErrorConstants;
import es.gobcan.coetl.service.FileService;
import es.gobcan.coetl.web.rest.dto.FileDTO;
import es.gobcan.coetl.web.rest.mapper.FileMapper;
import es.gobcan.coetl.web.rest.util.ControllerUtil;
import es.gobcan.coetl.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping(FileResource.BASE_URL)
public class FileResource extends AbstractResource {

    private final Logger log = LoggerFactory.getLogger(FileResource.class);

    private static final String ENTITY_NAME = "file";
    private static final String SLASH = "/";
    public static final String BASE_URL = "/api/files";

    private final FileService fileService;

    private FileMapper fileMapper;

    public FileResource(FileService fileService, FileMapper fileMapper) {
        this.fileService = fileService;
        this.fileMapper = fileMapper;
    }

    @PostMapping
    @Timed
    @PreAuthorize("@secChecker.canManageFile(authentication)")
    public ResponseEntity<FileDTO> createFile(@RequestParam("file") MultipartFile file) throws URISyntaxException {
        log.debug("REST request to save a File : {}", file);
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.FICHERO_VACIO, "Uploaded file is empty")).body(null);
        }
        File result = fileService.create(file);
        FileDTO resultDTO = fileMapper.toDto(result);
        return ResponseEntity.created(new URI(BASE_URL + SLASH + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(resultDTO);
    }

    @PutMapping
    @Timed
    @PreAuthorize("@secChecker.canManageFile(authentication)")
    public ResponseEntity<FileDTO> updateFile(@Valid @RequestBody FileDTO fileDTO) {
        log.debug("REST request to update a File : {}", fileDTO);
        if (fileDTO.getId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ID_FALTA, "An id is required")).body(null);
        }
        File file = fileService.findOne(fileDTO.getId());
        file = fileMapper.update(file, fileDTO);
        file = fileService.save(file);
        FileDTO result = fileMapper.toDto(file);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fileDTO.getId().toString())).body(result);
    }

    @GetMapping("/{id}")
    @Timed
    @PreAuthorize("@secChecker.canReadFile(authentication)")
    public ResponseEntity<FileDTO> getFile(@PathVariable Long id) {
        log.debug("REST request to get a File : {}", id);
        File file = fileService.findOne(id);
        if (file == null) {
            return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.ENTIDAD_NO_ENCONTRADA, "Entity requested was not found")).build();
        }
        FileDTO fileDTO = fileMapper.toDto(file);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fileDTO));
    }

    @GetMapping(value = "/{id}/download", consumes = "*/*", produces = "*/*")
    @Timed
    @PreAuthorize("@secChecker.canReadFile(authentication)")
    public void downloadFile(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to download a File : {}", id);
        File file = fileService.findOne(id);
        if (file == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            ControllerUtil.download(file, response);
        }
    }

    @DeleteMapping("/{id}")
    @Timed
    @PreAuthorize("@secChecker.canManageFile(authentication)")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        log.debug("REST request to delete a File : {}", id);
        fileService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
