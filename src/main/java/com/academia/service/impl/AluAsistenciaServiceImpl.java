package com.academia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.academia.entity.AluAsistencia;
import com.academia.repository.AluAsistenciaRepository;
import com.academia.service.AluAsistenciaService;

@Service
public class AluAsistenciaServiceImpl implements AluAsistenciaService {

	@Autowired
    private AluAsistenciaRepository asistenciaRepository;

    @Override
    public List<AluAsistencia> findAll(Pageable page) {
        return asistenciaRepository.findAll(page).getContent();
    }

    @Override
    public List<AluAsistencia> findAll() {
        return asistenciaRepository.findAll();
    }

    @Override
    public List<AluAsistencia> findByEstado(String nombre, Pageable page) {
        return asistenciaRepository.findByEstadoContaining(nombre, page);
    }

    @Override
    public AluAsistencia findById(int id) {
        return asistenciaRepository.findById(id).orElse(null);
    }

    @Override
    public AluAsistencia save(AluAsistencia asistencia) {
        return asistenciaRepository.save(asistencia);
    }

    @Override
    public void delete(int id) {
    	asistenciaRepository.deleteById(id);
    }
}
