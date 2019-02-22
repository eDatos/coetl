package es.gobcan.istac.coetl.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import es.gobcan.istac.coetl.domain.File;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.service.FileService;
import es.gobcan.istac.coetl.web.rest.dto.FileDTO;
import es.gobcan.istac.coetl.web.rest.mapper.FileMapper;
import es.gobcan.istac.coetl.web.rest.util.ControllerUtil;
import es.gobcan.istac.coetl.web.rest.util.HeaderUtil;

@RestController
@RequestMapping(FileResource.BASE_URI)
public class FileResource extends AbstractResource {

    private final Logger log = LoggerFactory.getLogger(FileResource.class);

    private static final String ENTITY_NAME = "file";
    private static final String SLASH = "/";
    public static final String BASE_URI = "/api/files";

    private final FileService fileService;

    private FileMapper fileMapper;

    public FileResource(FileService fileService, FileMapper fileMapper) {
        this.fileService = fileService;
        this.fileMapper = fileMapper;
    }

    @PostMapping
    @Timed
    @PreAuthorize("@secChecker.canManageFile(authentication)")
    public ResponseEntity<FileDTO> create(@RequestParam("file") MultipartFile file) throws URISyntaxException {
        log.debug("REST request to save a File : {}", file);
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, ErrorConstants.FICHERO_UNICO_VACIO, "Uploaded file is empty")).body(null);
        }
        File result = fileService.create(file);
        FileDTO resultDTO = fileMapper.toDto(result);
        return ResponseEntity.created(new URI(BASE_URI + SLASH + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(resultDTO);
    }

    @GetMapping(value = "/{id}/download", consumes = "*/*", produces = "*/*")
    @Timed
    @PreAuthorize("@secChecker.canReadFile(authentication)")
    public void download(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to download a File : {}", id);
        File file = fileService.findOne(id);
        if (file == null) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            ControllerUtil.download(file, response);
        }
    }
}
