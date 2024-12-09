package com.academia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.academia.entity.AluAsistencia;

import java.util.List;

@Repository
public interface AluAsistenciaRepository extends JpaRepository<AluAsistencia, Integer> {
    List<AluAsistencia> findByEstadoContaining(String estado, org.springframework.data.domain.Pageable page);
}