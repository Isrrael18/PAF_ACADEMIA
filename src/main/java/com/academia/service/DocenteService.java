package com.academia.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import com.academia.entity.Docente;

public interface DocenteService {
	List<Docente> findAll(Pageable page);

	List<Docente> findAll();

	List<Docente> findByNombre(String nombre, Pageable page);

	Docente findById(int id);

	Docente save(Docente docente);

	void delete(int id);
}
