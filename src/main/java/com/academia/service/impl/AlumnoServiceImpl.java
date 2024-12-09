package com.academia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.academia.entity.Alumno;
import com.academia.repository.AlumnoRepository;
import com.academia.service.AlumnoService;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoRepository alumnoRepository;

    @Override
    public List<Alumno> findAll(Pageable page) {
        return alumnoRepository.findAll(page).getContent();
    }

    @Override
    public List<Alumno> findAll() {
        return alumnoRepository.findAll();
    }

    @Override
    public List<Alumno> findByNombre(String nombre, Pageable page) {
        return alumnoRepository.findByNombreContaining(nombre, page);
    }

    @Override
    public Alumno findById(int id) {
        return alumnoRepository.findById(id).orElse(null);
    }

    @Override
    public Alumno save(Alumno estudiante) {
        return alumnoRepository.save(estudiante);
    }

    @Override
    public void delete(int id) {
    	alumnoRepository.deleteById(id);
    }
}
