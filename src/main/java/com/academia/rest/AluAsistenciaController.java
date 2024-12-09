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

import com.academia.converter.AluAsistenciaConverter;
import com.academia.dto.AluAsistenciaDto;
import com.academia.entity.Alumno;
import com.academia.entity.AluAsistencia;
import com.academia.service.AlumnoService;
import com.academia.service.AluAsistenciaService;
import com.academia.service.PdfService;
import com.academia.util.WrapperResponse;

@RestController
@RequestMapping("/v1/aluAsistencias")
public class AluAsistenciaController {

	@Autowired
	private AluAsistenciaService service;

	@Autowired
	private AlumnoService alumnoService;

	@Autowired
	private AluAsistenciaConverter converter;

	@Autowired
	private PdfService pdfService;

	@GetMapping
	public ResponseEntity<List<AluAsistenciaDto>> findAll(
			@RequestParam(value = "offset", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(value = "limit", required = false, defaultValue = "5") int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize);
		List<AluAsistenciaDto> aluAsistencias = converter.fromEntity(service.findAll(page));

		return new WrapperResponse(true, "success", aluAsistencias).createResponse(HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<AluAsistenciaDto> create(@RequestBody AluAsistenciaDto aluAsistencias) {
		AluAsistencia aluAsistenciaEntity = converter.fromDTO(aluAsistencias);
		AluAsistenciaDto registro = converter.fromEntity(service.save(aluAsistenciaEntity));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AluAsistenciaDto> update(@PathVariable("id") int id, @RequestBody AluAsistenciaDto aluAsistencias) {
		AluAsistencia aluAsistenciaEntity = converter.fromDTO(aluAsistencias);
		AluAsistenciaDto registro = converter.fromEntity(service.save(aluAsistenciaEntity));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable("id") int id) {
		service.delete(id);
		return new WrapperResponse(true, "success", null).createResponse(HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AluAsistenciaDto> findById(@PathVariable("id") int id) {
		AluAsistenciaDto registro = converter.fromEntity(service.findById(id));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}

	@GetMapping("/report")
	public ResponseEntity<byte[]> generateReport() {
		List<AluAsistenciaDto> aluAsistencias = converter.fromEntity(service.findAll());
		List<Alumno> alumnos = alumnoService.findAll();
		Map<Integer, String> alumnoMap = alumnos.stream().collect(
				Collectors.toMap(Alumno::getId, alumno -> alumno.getApellido() + ", " + alumno.getNombre()));
		
		for (AluAsistenciaDto aluAsistencia : aluAsistencias) {
			String alumnoNombre = alumnoMap.get(aluAsistencia.getAlumnoId());
			aluAsistencia.setAlumnoNombre(alumnoNombre);
		}
		LocalDateTime fecha = LocalDateTime.now();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String fechaHora = fecha.format(formato);

		Context context = new Context();
		context.setVariable("registros", aluAsistencias);
		context.setVariable("fecha", fechaHora);

		byte[] pdfBytes = pdfService.createPdf("aluAsistenciaReport", context);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("inline", "reporte_asistencia_alumno.pdf");

		return ResponseEntity.ok().headers(headers).body(pdfBytes);
	}

}