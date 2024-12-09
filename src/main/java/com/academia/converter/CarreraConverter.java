package com.academia.converter;

import org.springframework.stereotype.Component;

import com.academia.dto.CarreraDto;
import com.academia.entity.Carrera;

@Component
public class CarreraConverter extends AbstractConverter<Carrera, CarreraDto>{

	@Override
    public CarreraDto fromEntity(Carrera entity) {
        if (entity == null) return null;
        return CarreraDto.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .build();
    }
	
	@Override
    public Carrera fromDTO(CarreraDto dto) {
        if (dto == null) return null;
        return Carrera.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .build();
    }
}
