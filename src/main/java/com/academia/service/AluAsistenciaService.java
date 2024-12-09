package com.academia.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import com.academia.entity.AluAsistencia;

public interface AluAsistenciaService {
	List<AluAsistencia> findAll(Pageable page);

	List<AluAsistencia> findAll();

	List<AluAsistencia> findByEstado(String estado, Pageable page);

	AluAsistencia findById(int id);

	AluAsistencia save(AluAsistencia asistencia);

	void delete(int id);
}
