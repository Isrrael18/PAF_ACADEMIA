package com.academia.converter;

import org.springframework.stereotype.Component;

import com.academia.dto.AluAsistenciaDto;
import com.academia.entity.AluAsistencia;

@Component
public class AluAsistenciaConverter extends AbstractConverter<AluAsistencia, AluAsistenciaDto>{

	@Override
    public AluAsistenciaDto fromEntity(AluAsistencia entity) {
        if (entity == null) return null;
        return AluAsistenciaDto.builder()
                .id(entity.getId())
                .alumnoId(entity.getAlumno_id())
                .estado(entity.getEstado())
                .fecha(entity.getFecha())
                .build();
    }

	
	@Override
    public AluAsistencia fromDTO(AluAsistenciaDto dto) {
        if (dto == null) return null;
        return AluAsistencia.builder()
                .id(dto.getId())
                .alumno_id(dto.getAlumnoId())
                .estado(dto.getEstado())
                .fecha(dto.getFecha())
                .build();
    }
}
