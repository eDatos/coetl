package com.arte.application.template.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.arte.application.template.domain.Documento;

public interface DocumentoService {

    Documento save(Documento documento);

    List<Documento> findAll();

    Documento findOne(Long id);

    void delete(Long id);

    Documento create(MultipartFile file);
}
