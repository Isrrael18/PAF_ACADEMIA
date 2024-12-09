package com.academia.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.academia.entity.Curso;
import com.academia.repository.CursoRepository;
import com.academia.service.CursoService;

@Service
public class CursoServiceImpl implements CursoService {

	 @Autowired
	    private CursoRepository cursoRepository; 

	    @Override
	    public List<Curso> findAll(Pageable page) {
	        return cursoRepository.findAll(page).getContent();
	    }

	    @Override
	    public List<Curso> findAll() {
	        return cursoRepository.findAll();
	    }

	    @Override
	    public List<Curso> findByNombre(String nombre, Pageable page) {
	        return cursoRepository.findByNombreContaining(nombre, page); 
	    }

	    @Override
	    public Curso findById(int id) {
	        return cursoRepository.findById(id).orElse(null);
	    }

	    @Override
	    public Curso save(Curso curso) {
	        return cursoRepository.save(curso);
	    }

	    @Override
	    public void delete(int id) {
	        cursoRepository.deleteById(id);
	    }
	}
