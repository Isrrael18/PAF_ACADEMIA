package com.academia.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academia.converter.UsuarioConverter;
import com.academia.dto.UsuarioDto;
import com.academia.entity.Usuario;
import com.academia.service.UsuarioService;
import com.academia.util.WrapperResponse;


@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {
	@Autowired
	private UsuarioService service;
	
	@Autowired
	private UsuarioConverter converter;
	
	
	@GetMapping
	public ResponseEntity<List<UsuarioDto>> findAll(){				
		List<UsuarioDto> registros = converter.fromEntity(service.findAll());		
		return new WrapperResponse(true, "success", registros).createResponse(HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<UsuarioDto> create (@RequestBody Usuario usuario){
		UsuarioDto registro = converter.fromEntity(service.save(usuario));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.CREATED);
	}
	
	
	@PutMapping("/{id}/deactivate")
	public ResponseEntity<UsuarioDto> deactivate(@PathVariable("id") int id){
		UsuarioDto registro = converter.fromEntity(service.deactivate(id));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}
	@PutMapping("/{id}/activate")
	public ResponseEntity<UsuarioDto> activate(@PathVariable("id") int id){
		UsuarioDto registro=converter.fromEntity(service.activate(id));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}
	@PutMapping("/{id}")
	public ResponseEntity<UsuarioDto> update(@PathVariable("id") int id,@RequestBody Usuario usuario){
		UsuarioDto registro = converter.fromEntity(service.save(usuario));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDto> findById(@PathVariable("id") int id){
		UsuarioDto registro=converter.fromEntity(service.findById(id));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}
}
