package com.academia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.academia.entity.Curso;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {
    List<Curso> findByNombreContaining(String nombre, org.springframework.data.domain.Pageable page);
    List<Curso> findByHoras(int horas);
}
