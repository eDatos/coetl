package com.arte.application.template.web.rest.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import com.arte.application.template.domain.Documento;
import com.arte.application.template.web.rest.errors.CustomParameterizedException;
import com.arte.application.template.web.rest.errors.ErrorConstants;

public final class ControllerUtil {

    private static final Logger log = LoggerFactory.getLogger(ControllerUtil.class);
    
    private ControllerUtil() {
        super();
    }
    
    public static void download(Documento documento, HttpServletResponse response) {
        try (InputStream is = documento.getData().getBinaryStream()) {
            copyContentToResponse(is, documento.getName(), documento.getDataContentType(), documento.getLength(), response);
        } catch (IOException | SQLException e) {
            log.error("Exception obtaining the file {}", documento.getId(), e);
            throw new CustomParameterizedException(String.format("Exception obtaining the file %s", documento.getId()), ErrorConstants.FICHERO_NO_ENCONTRADO);
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