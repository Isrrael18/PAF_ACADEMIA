package com.academia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.academia.entity.DocAsistencia;

import java.util.List;

@Repository
public interface DocAsistenciaRepository extends JpaRepository<DocAsistencia, Integer> {
    List<DocAsistencia> findByEstadoContaining(String estado, org.springframework.data.domain.Pageable page);
}