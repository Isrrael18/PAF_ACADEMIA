package com.academia.validator;

import com.academia.entity.Curso;
import com.academia.exception.ValidateException;

public class CursoValidator {
    public static void validate(Curso curso) {
        if (curso.getNombre() == null || curso.getNombre().trim().isEmpty()) {
            throw new ValidateException("El nombre del curso es requerido");
        }
        if (curso.getNombre().length() > 100) {
            throw new ValidateException("El nombre del curso no debe tener más de 100 caracteres");
        }
        if (curso.getHoras() <= 0) {
            throw new ValidateException("Las horas del curso deben ser mayores a cero");
        }
    }
}
