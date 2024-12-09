package com.academia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.academia.entity.Carrera;
import com.academia.repository.CarreraRepository;
import com.academia.service.CarreraService;

@Service
public class CarreraServiceImpl implements CarreraService {

	@Autowired
    private CarreraRepository carreraRepository; 

    @Override
    public List<Carrera> findAll(Pageable page) {
        return carreraRepository.findAll(page).getContent();
    }

    @Override
    public List<Carrera> findAll() {
        return carreraRepository.findAll();
    }

    @Override
    public List<Carrera> findByNombre(String nombre, Pageable page) {
        return carreraRepository.findByNombreContaining(nombre, page); 
    }

    @Override
    public Carrera findById(int id) {
        return carreraRepository.findById(id).orElse(null);
    }

    @Override
    public Carrera save(Carrera carrera) {
        return carreraRepository.save(carrera);
    }

    @Override
    public void delete(int id) {
    	carreraRepository.deleteById(id);
    }
}
