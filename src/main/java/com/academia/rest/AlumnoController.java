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

import com.academia.converter.AlumnoConverter;
import com.academia.dto.AlumnoDto;
import com.academia.entity.Alumno;
import com.academia.entity.Carrera;
import com.academia.service.AlumnoService;
import com.academia.service.CarreraService;
import com.academia.service.PdfService;
import com.academia.util.WrapperResponse;

@RestController
@RequestMapping("/v1/alumnos")
public class AlumnoController {

	@Autowired
	private AlumnoService service;

	@Autowired
	private CarreraService carreraService;

	@Autowired
	private AlumnoConverter converter;

	@Autowired
	private PdfService pdfService;

	@GetMapping
	public ResponseEntity<List<AlumnoDto>> findAll(
			@RequestParam(value = "offset", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(value = "limit", required = false, defaultValue = "5") int pageSize) {
		Pageable page = PageRequest.of(pageNumber, pageSize);
		List<AlumnoDto> alumnos = converter.fromEntity(service.findAll(page));

		return new WrapperResponse(true, "success", alumnos).createResponse(HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<AlumnoDto> create(@RequestBody AlumnoDto alumno) {
		Alumno alumnoEntity = converter.fromDTO(alumno);
		AlumnoDto registro = converter.fromEntity(service.save(alumnoEntity));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<AlumnoDto> update(@PathVariable("id") int id, @RequestBody AlumnoDto alumno) {
		Alumno alumnoEntity = converter.fromDTO(alumno);
		AlumnoDto registro = converter.fromEntity(service.save(alumnoEntity));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity delete(@PathVariable("id") int id) {
		service.delete(id);
		return new WrapperResponse(true, "success", null).createResponse(HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AlumnoDto> findById(@PathVariable("id") int id) {
		AlumnoDto registro = converter.fromEntity(service.findById(id));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}

	@GetMapping("/report")
	public ResponseEntity<byte[]> generateReport() {
		List<AlumnoDto> alumnos = converter.fromEntity(service.findAll());

		List<Carrera> carreras = carreraService.findAll();
		Map<Integer, String> carreraMap = carreras.stream()
				.collect(Collectors.toMap(Carrera::getId, Carrera::getNombre));

		for (AlumnoDto alumno : alumnos) {
			String carreraNombre = carreraMap.get(alumno.getCarreraId());
			alumno.setCarreraNombre(carreraNombre);
		}

		LocalDateTime fecha = LocalDateTime.now();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String fechaHora = fecha.format(formato);

		Context context = new Context();
		context.setVariable("registros", alumnos);
		context.setVariable("fecha", fechaHora);

		byte[] pdfBytes = pdfService.createPdf("alumnoReport", context);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("inline", "reporte_alumnos.pdf");

		return ResponseEntity.ok().headers(headers).body(pdfBytes);
	}
}
