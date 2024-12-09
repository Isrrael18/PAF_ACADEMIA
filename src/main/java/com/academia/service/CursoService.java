package com.academia.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import com.academia.entity.Curso;

public interface CursoService {
	List<Curso> findAll(Pageable page);

	List<Curso> findAll();

	List<Curso> findByNombre(String nombre, Pageable page);

	Curso findById(int id);

	Curso save(Curso curso);

	void delete(int id);
}
