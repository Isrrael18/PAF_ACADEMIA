package com.academia.validator;

import com.academia.entity.DocAsistencia;
import com.academia.exception.ValidateException;

public class DocAsistenciaValidator {

    public static void validate(DocAsistencia asistencia) {
        if (asistencia.getDocenteId() <= 0) {
            throw new ValidateException("Debe de haber un docente registrado.");
        }

        if (asistencia.getEstado() == null || asistencia.getEstado().trim().isEmpty()) {
            throw new ValidateException("El estado de la asistencia es requerido.");
        }
        if (!asistencia.getEstado().matches("^(A|F|FJ)$")) {
            throw new ValidateException("El estado de la asistencia debe ser 'presente' o 'ausente'.");
        }
        if (asistencia.getCreatedAt() != null && asistencia.getCreatedAt().after(new java.util.Date())) {
            throw new ValidateException("La fecha de creación no puede ser en el futuro.");
        }
        if (asistencia.getUpdatedAt() != null && asistencia.getUpdatedAt().after(new java.util.Date())) {
            throw new ValidateException("La fecha de actualización no puede ser en el futuro.");
        }
    }
}
