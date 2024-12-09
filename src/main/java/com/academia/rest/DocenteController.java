package com.academia.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.academia.converter.DocenteConverter;
import com.academia.dto.DocenteDto;
import com.academia.entity.Curso;
import com.academia.entity.Docente;
import com.academia.service.CursoService;
import com.academia.service.DocenteService;
import com.academia.service.PdfService;
import com.academia.util.WrapperResponse;

@RestController
@RequestMapping("/v1/docentes")
public class DocenteController {

	@Autowired
	private DocenteService service;

	@Autowired
	private CursoService cursoService;

	@Autowired
	private DocenteConverter converter;

	@Autowired
	private PdfService pdfService;

	@GetMapping
	public ResponseEntity<List<DocenteDto>> findAll(
			@RequestParam(value = "offset", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(value = "limit", required = false, defaultValue = "5") int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize);
		List<DocenteDto> docentes = converter.fromEntity(service.findAll(page));

		return new WrapperResponse(true, "success", docentes).createResponse(HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<DocenteDto> create(@RequestBody DocenteDto docente) {
		Docente docenteEntity = converter.fromDTO(docente);
		DocenteDto registro = converter.fromEntity(service.save(docenteEntity));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<DocenteDto> update(@PathVariable("id") int id, @RequestBody DocenteDto docente) {
		Docente docenteEntity = converter.fromDTO(docente);
		DocenteDto registro = converter.fromEntity(service.save(docenteEntity));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable("id") int id) {
		service.delete(id);
		return new WrapperResponse(true, "success", null).createResponse(HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DocenteDto> findById(@PathVariable("id") int id) {
		DocenteDto registro = converter.fromEntity(service.findById(id));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}

	@GetMapping("/report")
	public ResponseEntity<byte[]> generateReport() {
		List<DocenteDto> docentes = converter.fromEntity(service.findAll());

		List<Curso> cursos = cursoService.findAll();
		Map<Integer, String> cursoMap = cursos.stream().collect(Collectors.toMap(Curso::getId, Curso::getNombre));

		for (DocenteDto docente : docentes) {
			String cursoNombre = cursoMap.get(docente.getCursoId());
			docente.setCursoNombre(cursoNombre);
		}

		LocalDateTime fecha = LocalDateTime.now();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String fechaHora = fecha.format(formato);

		Context context = new Context();
		context.setVariable("registros", docentes);
		context.setVariable("fecha", fechaHora);

		byte[] pdfBytes = pdfService.createPdf("docenteReport", context);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("inline", "reporte_docentes.pdf");

		return ResponseEntity.ok().headers(headers).body(pdfBytes);
	}

}