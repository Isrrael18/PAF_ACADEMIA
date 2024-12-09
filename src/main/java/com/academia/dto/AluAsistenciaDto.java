package com.academia.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AluAsistenciaDto {
    private int id;
    private String estado;
    private Date fecha;
    private int alumnoId;
    private String alumnoNombre;
}