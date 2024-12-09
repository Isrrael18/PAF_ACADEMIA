package com.academia.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import com.academia.entity.Alumno;

public interface AlumnoService {
	List<Alumno> findAll(Pageable page);

	List<Alumno> findAll();

	List<Alumno> findByNombre(String nombre, Pageable page);

	Alumno findById(int id);

	Alumno save(Alumno alumno);

	void delete(int id);
}
