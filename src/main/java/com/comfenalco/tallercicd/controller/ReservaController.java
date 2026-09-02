package com.comfenalco.tallercicd.controller;

import com.comfenalco.tallercicd.exception.*;
import com.comfenalco.tallercicd.service.ReservaService;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reservas")
@Validated
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping("/cotizar")
    public ResponseEntity<Map<String, Object>> cotizar(
            @RequestParam Long actividadId,
            @RequestParam @Min(1) int numPersonas) {

        double total = reservaService.cotizar(actividadId, numPersonas);
        return ResponseEntity.ok(Map.of(
                "actividadId", actividadId,
                "numPersonas", numPersonas,
                "total", total));
    }

    @ExceptionHandler(ActividadNoEncontradaException.class)
    public ResponseEntity<Map<String, String>> noEncontrada(ActividadNoEncontradaException e) {
        return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(CupoInsuficienteException.class)
    public ResponseEntity<Map<String, String>> cupo(CupoInsuficienteException e) {
        return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
    }
}
