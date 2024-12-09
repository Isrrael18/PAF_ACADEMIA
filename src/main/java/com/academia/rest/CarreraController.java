package com.academia.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import com.academia.converter.CarreraConverter;
import com.academia.dto.CarreraDto;
import com.academia.entity.Carrera;
import com.academia.service.CarreraService;
import com.academia.service.PdfService;
import com.academia.util.WrapperResponse;


@RestController
@RequestMapping("/v1/carreras")

public class CarreraController {
	@Autowired
	private CarreraService service;
	
	@Autowired
	private CarreraConverter converter;
	
	@Autowired
	private PdfService pdfService;
	
	@GetMapping
	public ResponseEntity<List<CarreraDto>> findAll(
			@RequestParam(value = "offset", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(value = "limit", required = false, defaultValue = "5") int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize);
		List<CarreraDto> carreras = converter.fromEntity(service.findAll(page));

		return new WrapperResponse(true, "success", carreras).createResponse(HttpStatus.OK);
	}

	
	@PostMapping
	public ResponseEntity<CarreraDto> create(@RequestBody CarreraDto carrera) {
		Carrera carreraEntity = converter.fromDTO(carrera);
		CarreraDto registro = converter.fromEntity(service.save(carreraEntity));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CarreraDto> update(@PathVariable("id") int id, @RequestBody CarreraDto carrera) {
		Carrera carreraEntity = converter.fromDTO(carrera);
		CarreraDto registro = converter.fromEntity(service.save(carreraEntity));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable("id") int id) {
		service.delete(id);
		return new WrapperResponse(true, "success", null).createResponse(HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CarreraDto> findById(@PathVariable("id") int id) {
		CarreraDto registro = converter.fromEntity(service.findById(id));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}
	
	
	@GetMapping("/report")
	public ResponseEntity<byte[]> generateReport() {
		List<CarreraDto> carreras = converter.fromEntity(service.findAll());
	    
	    LocalDateTime fecha = LocalDateTime.now();
	    DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String fechaHora = fecha.format(formato);
	    
	    Context context = new Context();
	    context.setVariable("registros", carreras);
	    context.setVariable("fecha", fechaHora);
	    
	    byte[] pdfBytes = pdfService.createPdf("carreraReport", context);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDispositionFormData("inline", "reporte_carreras.pdf");

	    return ResponseEntity.ok().headers(headers).body(pdfBytes);	    
	}
	
}