package com.academia.dto;

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
public class DocenteDto {
    private int id;
    private String nombre;
    private String apellido;
    private int cursoId;
    private String cursoNombre;
    private String telefono;
    private String email;
}
