package com.comfenalco.tallercicd.repository;

import com.comfenalco.tallercicd.model.Actividad;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ActividadRepository {

    private final Map<Long, Actividad> datos = new ConcurrentHashMap<>();

    public ActividadRepository() {
        datos.put(1L, new Actividad(1L, "City tour Cartagena", 80_000, 20));
        datos.put(2L, new Actividad(2L, "Islas del Rosario", 150_000, 8));
        datos.put(3L, new Actividad(3L, "Volcán del Totumo", 60_000, 0));
    }

    public Optional<Actividad> buscarPorId(Long id) {
        return Optional.ofNullable(datos.get(id));
    }

    public List<Actividad> listar() {
        return List.copyOf(datos.values());
    }
}
