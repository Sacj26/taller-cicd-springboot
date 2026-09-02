package com.comfenalco.tallercicd.controller;

import com.comfenalco.tallercicd.exception.ActividadNoEncontradaException;
import com.comfenalco.tallercicd.service.ReservaService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservaController.class)
@DisplayName("ReservaController - capa web")
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservaService reservaService;

    @Test
    @DisplayName("Devuelve 200 y el total cotizado")
    void debeDevolver200ConElTotal() throws Exception {
        when(reservaService.cotizar(1L, 6)).thenReturn(540_000.0);

        mockMvc.perform(get("/api/reservas/cotizar")
                        .param("actividadId", "1")
                        .param("numPersonas", "6"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(540_000.0));
    }

    @Test
    @DisplayName("Devuelve 404 si la actividad no existe")
    void debeDevolver404() throws Exception {
        when(reservaService.cotizar(eq(99L), anyInt()))
                .thenThrow(new ActividadNoEncontradaException(99L));

        mockMvc.perform(get("/api/reservas/cotizar")
                        .param("actividadId", "99")
                        .param("numPersonas", "2"))
                .andExpect(status().isNotFound());
    }
}
