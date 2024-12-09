package com.academia.validator;

import com.academia.entity.Alumno;
import com.academia.exception.ValidateException;

public class AlumnoValidator {
    public static void validate(Alumno alumno) {
        if (alumno.getNombre() == null || alumno.getNombre().trim().isEmpty()) {
            throw new ValidateException("El nombre del alumno es requerido");
        }
        if (alumno.getNombre().length() > 50) {
            throw new ValidateException("El nombre del alumno no debe tener más de 50 caracteres");
        }
        if (alumno.getApellido() == null || alumno.getApellido().trim().isEmpty()) {
            throw new ValidateException("El apellido del alumno es requerido");
        }
        if (alumno.getApellido().length() > 50) {
            throw new ValidateException("El apellido del alumno no debe tener más de 50 caracteres");
        }
        if (alumno.getDni() == null || alumno.getDni().trim().isEmpty()) {
            throw new ValidateException("El DNI del alumno es requerido");
        }
        if (alumno.getDni().length() != 8) {
            throw new ValidateException("El DNI debe tener exactamente 8 caracteres");
        }
        if (!alumno.getDni().matches("\\d+")) {
            throw new ValidateException("El DNI solo debe contener números");
        }
        if (alumno.getEmail() != null && alumno.getEmail().length() > 100) {
            throw new ValidateException("El email no debe tener más de 100 caracteres");
        }
        if (alumno.getTelefono() != null && alumno.getTelefono().length() > 15) {
            throw new ValidateException("El teléfono no debe tener más de 15 caracteres");
        }
    }
}
