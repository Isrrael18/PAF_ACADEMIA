package com.academia.converter;

import org.springframework.stereotype.Component;

import com.academia.dto.DocAsistenciaDto;
import com.academia.entity.DocAsistencia;

@Component
public class DocAsistenciaConverter extends AbstractConverter<DocAsistencia, DocAsistenciaDto>{

	@Override
    public DocAsistenciaDto fromEntity(DocAsistencia entity) {
        if (entity == null) return null;
        return DocAsistenciaDto.builder()
                .id(entity.getId())
                .docenteId(entity.getDocenteId())
                .estado(entity.getEstado())
                .fecha(entity.getFecha())
                .build();
    }

	
	@Override
    public DocAsistencia fromDTO(DocAsistenciaDto dto) {
        if (dto == null) return null;
        return DocAsistencia.builder()
                .id(dto.getId())
                .docenteId(dto.getDocenteId())
                .estado(dto.getEstado())
                .fecha(dto.getFecha())
                .build();
    }
}
