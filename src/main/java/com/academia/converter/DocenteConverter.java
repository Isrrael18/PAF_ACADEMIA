package com.academia.converter;

import org.springframework.stereotype.Component;

import com.academia.dto.DocenteDto;
import com.academia.entity.Docente;

@Component
public class DocenteConverter extends AbstractConverter<Docente, DocenteDto>{

	@Override
    public DocenteDto fromEntity(Docente entity) {
        if (entity == null) return null;
        return DocenteDto.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .apellido(entity.getApellido())
                .cursoId(entity.getCursoId())
                .telefono(entity.getTelefono())
                .email(entity.getEmail())
                .build();
    }

	
	@Override
    public Docente fromDTO(DocenteDto dto) {
        if (dto == null) return null;
        return Docente.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .cursoId(dto.getCursoId())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .build();
    }
}
