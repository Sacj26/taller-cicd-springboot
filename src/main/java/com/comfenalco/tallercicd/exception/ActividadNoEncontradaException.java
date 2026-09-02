package com.comfenalco.tallercicd.exception;

public class ActividadNoEncontradaException extends RuntimeException {
    public ActividadNoEncontradaException(Long id) {
        super("No existe la actividad con id " + id);
    }
}
