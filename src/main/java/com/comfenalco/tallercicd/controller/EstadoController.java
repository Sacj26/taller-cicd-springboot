package com.comfenalco.tallercicd.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EstadoController {

    @GetMapping("/estado")
    public Map<String, String> estado() {
        return Map.of("estado", "activo", "servicio", "taller-cicd");
    }
}
