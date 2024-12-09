package com.academia.converter;

import org.springframework.stereotype.Component;

import com.academia.dto.AlumnoDto;
import com.academia.entity.Alumno;

@Component
public class AlumnoConverter extends AbstractConverter <Alumno, AlumnoDto>{

	@Override
    public AlumnoDto fromEntity(Alumno entity) {
        if (entity == null) return null;
        return AlumnoDto.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .apellido(entity.getApellido())
                .dni(entity.getDni())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .carreraId(entity.getCarreraId())
                .build();
    }

	
	@Override
    public Alumno fromDTO(AlumnoDto dto) {
        if (dto == null) return null;
        return Alumno.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .dni(dto.getDni())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .carreraId(dto.getCarreraId())
                .build();
    }
}
