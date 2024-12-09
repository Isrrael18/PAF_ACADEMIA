package com.academia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.academia.entity.Docente;
import com.academia.repository.DocenteRepository;
import com.academia.service.DocenteService;

@Service
public class DocenteServiceImpl implements DocenteService {

	@Autowired
    private DocenteRepository docenteRepository;

    @Override
    public List<Docente> findAll(Pageable page) {
        return docenteRepository.findAll(page).getContent();
    }

    @Override
    public List<Docente> findAll() {
        return docenteRepository.findAll();
    }

    @Override
    public List<Docente> findByNombre(String nombre, Pageable page) {
        return docenteRepository.findByNombreContaining(nombre, page);
    }

    @Override
    public Docente findById(int id) {
        return docenteRepository.findById(id).orElse(null);
    }

    @Override
    public Docente save(Docente profesor) {
        return docenteRepository.save(profesor);
    }

    @Override
    public void delete(int id) {
    	docenteRepository.deleteById(id);
    }
}
