package com.academia.converter;

import org.springframework.stereotype.Component;

import com.academia.dto.CursoDto;
import com.academia.entity.Curso;

@Component
public class CursoConverter extends AbstractConverter<Curso, CursoDto> {

	@Override	
    public CursoDto fromEntity(Curso entity) {
        if (entity == null) return null;
        return CursoDto.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .horas(entity.getHoras())
                .build();
    }
	
	
	@Override
    public Curso fromDTO(CursoDto dto) {
        if (dto == null) return null;
        return Curso.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .horas(dto.getHoras())
                .build();
    }
}
