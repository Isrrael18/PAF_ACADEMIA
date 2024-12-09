package com.academia.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import com.academia.entity.DocAsistencia;

public interface DocAsistenciaService {
	List<DocAsistencia> findAll(Pageable page);

	List<DocAsistencia> findAll();

	List<DocAsistencia> findByEstado(String estado, Pageable page);

	DocAsistencia findById(int id);

	DocAsistencia save(DocAsistencia asistencia);

	void delete(int id);
}
