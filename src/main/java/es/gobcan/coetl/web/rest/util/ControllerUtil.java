package es.gobcan.coetl.web.rest.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import es.gobcan.coetl.domain.File;
import es.gobcan.coetl.errors.ErrorConstants;
import es.gobcan.coetl.errors.util.CustomExceptionUtil;

public final class ControllerUtil {

    private static final Logger log = LoggerFactory.getLogger(ControllerUtil.class);

    private ControllerUtil() {
        super();
    }

    public static void download(File file, HttpServletResponse response) {
        try (InputStream is = file.getData().getBinaryStream()) {
            copyContentToResponse(is, file.getName(), file.getDataContentType(), file.getLength(), response);
        } catch (IOException | SQLException e) {
            log.error("Exception obtaining the file {}", file.getId(), e);
            final String message = String.format("Exception obtaining the file %s", file.getId());
            final String code = ErrorConstants.FICHERO_NO_ENCONTRADO;
            CustomExceptionUtil.throwCustomParameterizedException(message, e, code);
        }
    }

    private static void copyContentToResponse(InputStream inputStream, String name, String contentType, Long length, HttpServletResponse response) throws IOException {
        if (length != null) {
            response.setContentLength(length.intValue());
        }
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", name));
        try (OutputStream os = response.getOutputStream()) {
            StreamUtils.copy(inputStream, os);
        }
    }
}