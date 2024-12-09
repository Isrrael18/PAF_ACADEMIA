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

import com.academia.converter.DocAsistenciaConverter;
import com.academia.dto.DocAsistenciaDto;
import com.academia.entity.Docente;
import com.academia.entity.DocAsistencia;
import com.academia.service.DocenteService;
import com.academia.service.DocAsistenciaService;
import com.academia.service.PdfService;
import com.academia.util.WrapperResponse;

@RestController
@RequestMapping("/v1/docAsistencias")
public class DocAsistenciaController {

	@Autowired
	private DocAsistenciaService service;

	@Autowired
	private DocenteService docenteService;

	@Autowired
	private DocAsistenciaConverter converter;

	@Autowired
	private PdfService pdfService;

	@GetMapping
	public ResponseEntity<List<DocAsistenciaDto>> findAll(
			@RequestParam(value = "offset", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(value = "limit", required = false, defaultValue = "5") int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize);
		List<DocAsistenciaDto> docAsistencias = converter.fromEntity(service.findAll(page));

		return new WrapperResponse(true, "success", docAsistencias).createResponse(HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<DocAsistenciaDto> create(@RequestBody DocAsistenciaDto docAsistencias) {
		DocAsistencia docAsistenciaEntity = converter.fromDTO(docAsistencias);
		DocAsistenciaDto registro = converter.fromEntity(service.save(docAsistenciaEntity));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<DocAsistenciaDto> update(@PathVariable("id") int id,
			@RequestBody DocAsistenciaDto docAsistencias) {
		DocAsistencia docAsistenciaEntity = converter.fromDTO(docAsistencias);
		DocAsistenciaDto registro = converter.fromEntity(service.save(docAsistenciaEntity));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable("id") int id) {
		service.delete(id);
		return new WrapperResponse(true, "success", null).createResponse(HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DocAsistenciaDto> findById(@PathVariable("id") int id) {
		DocAsistenciaDto registro = converter.fromEntity(service.findById(id));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}

	@GetMapping("/report")
	public ResponseEntity<byte[]> generateReport() {
		List<DocAsistenciaDto> docAsistencias = converter.fromEntity(service.findAll());
		List<Docente> docentes = docenteService.findAll();
		Map<Integer, String> docenteMap = docentes.stream().collect(
				Collectors.toMap(Docente::getId, docente -> docente.getApellido() + ", " + docente.getNombre()));

		for (DocAsistenciaDto docAsistencia : docAsistencias) {
			String docenteNombre = docenteMap.get(docAsistencia.getDocenteId());
			docAsistencia.setDocenteNombre(docenteNombre);
		}
		LocalDateTime fecha = LocalDateTime.now();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String fechaHora = fecha.format(formato);

		Context context = new Context();
		context.setVariable("registros", docAsistencias);
		context.setVariable("fecha", fechaHora);

		byte[] pdfBytes = pdfService.createPdf("docAsistenciaReport", context);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("inline", "reporte_asistencia_docente.pdf");

		return ResponseEntity.ok().headers(headers).body(pdfBytes);
	}

}