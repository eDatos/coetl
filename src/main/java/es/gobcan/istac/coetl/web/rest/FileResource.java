package es.gobcan.istac.coetl.web.rest;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import es.gobcan.istac.coetl.domain.File;
import es.gobcan.istac.coetl.service.FileService;
import es.gobcan.istac.coetl.web.rest.util.ControllerUtil;

@RestController
@RequestMapping(FileResource.BASE_URI)
public class FileResource extends AbstractResource {

    private final Logger log = LoggerFactory.getLogger(FileResource.class);

    public static final String BASE_URI = "/api/files";

    private final FileService fileService;

    public FileResource(FileService fileService) {
        this.fileService = fileService;
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
