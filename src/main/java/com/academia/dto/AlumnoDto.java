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
public class AlumnoDto {
    private int id;
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    private int carreraId;
    private String carreraNombre;
}
