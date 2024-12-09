package com.academia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.academia.entity.DocAsistencia;
import com.academia.repository.DocAsistenciaRepository;
import com.academia.service.DocAsistenciaService;

@Service
public class DocAsistenciaServiceImpl implements DocAsistenciaService {

	@Autowired
    private DocAsistenciaRepository asistenciaRepository;

    @Override
    public List<DocAsistencia> findAll(Pageable page) {
        return asistenciaRepository.findAll(page).getContent();
    }

    @Override
    public List<DocAsistencia> findAll() {
        return asistenciaRepository.findAll();
    }

    @Override
    public List<DocAsistencia> findByEstado(String nombre, Pageable page) {
        return asistenciaRepository.findByEstadoContaining(nombre, page);
    }

    @Override
    public DocAsistencia findById(int id) {
        return asistenciaRepository.findById(id).orElse(null);
    }

    @Override
    public DocAsistencia save(DocAsistencia asistencia) {
        return asistenciaRepository.save(asistencia);
    }

    @Override
    public void delete(int id) {
    	asistenciaRepository.deleteById(id);
    }
}
