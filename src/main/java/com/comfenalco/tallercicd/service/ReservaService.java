package com.comfenalco.tallercicd.service;

import com.comfenalco.tallercicd.exception.*;
import com.comfenalco.tallercicd.model.Actividad;
import com.comfenalco.tallercicd.repository.ActividadRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    private final ActividadRepository actividadRepository;

    public ReservaService(ActividadRepository actividadRepository) {
        this.actividadRepository = actividadRepository;
    }

    /**
     * Descuento por volumen:
     *   1-4 personas   -> 0%
     *   5-9 personas   -> 10%
     *   10+ personas   -> 15%
     */
    public double calcularDescuento(int numPersonas) {
        if (numPersonas <= 0) {
            throw new IllegalArgumentException("El número de personas debe ser mayor que cero");
        }
        if (numPersonas >= 10) return 0.15;
        if (numPersonas >= 5)  return 0.10;
        return 0.0;
    }

    public double cotizar(Long actividadId, int numPersonas) {
        Actividad actividad = actividadRepository.buscarPorId(actividadId)
                .orElseThrow(() -> new ActividadNoEncontradaException(actividadId));

        if (numPersonas > actividad.cupoDisponible()) {
            throw new CupoInsuficienteException(
                "Cupo disponible: " + actividad.cupoDisponible() + ", solicitado: " + numPersonas);
        }

        double subtotal = actividad.precioBase() * numPersonas;
        return subtotal * (1 - calcularDescuento(numPersonas));
    }
}
