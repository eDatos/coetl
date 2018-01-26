package com.arte.application.template.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arte.application.template.domain.Documento;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

}
