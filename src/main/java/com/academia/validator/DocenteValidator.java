package com.academia.validator;

import com.academia.entity.Docente;
import com.academia.exception.ValidateException;

public class DocenteValidator {
    public static void validate(Docente docente) {
        if (docente.getNombre() == null || docente.getNombre().trim().isEmpty()) {
            throw new ValidateException("El nombre del docente es requerido");
        }
        if (docente.getNombre().length() > 50) {
            throw new ValidateException("El nombre del docente no debe tener más de 50 caracteres");
        }
        if (docente.getApellido() == null || docente.getApellido().trim().isEmpty()) {
            throw new ValidateException("El apellido del docente es requerido");
        }
        if (docente.getApellido().length() > 50) {
            throw new ValidateException("El apellido del docente no debe tener más de 50 caracteres");
        }
        if (docente.getTelefono() != null && docente.getTelefono().length() > 15) {
            throw new ValidateException("El teléfono no debe tener más de 15 caracteres");
        }
        if (docente.getEmail() != null && docente.getEmail().length() > 100) {
            throw new ValidateException("El email no debe tener más de 100 caracteres");
        }
        if (docente.getEmail() != null && !docente.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new ValidateException("El email no es válido");
        }
    }
}
