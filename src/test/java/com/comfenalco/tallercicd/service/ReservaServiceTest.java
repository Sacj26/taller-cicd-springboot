package com.comfenalco.tallercicd.service;

import com.comfenalco.tallercicd.exception.*;
import com.comfenalco.tallercicd.model.Actividad;
import com.comfenalco.tallercicd.repository.ActividadRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ReservaService - lógica de cotización")
class ReservaServiceTest {

    @Mock
    private ActividadRepository actividadRepository;

    @InjectMocks
    private ReservaService reservaService;

    @ParameterizedTest(name = "{0} personas -> {1}% de descuento")
    @CsvSource({ "1, 0.0", "4, 0.0", "5, 0.10", "9, 0.10", "10, 0.15", "25, 0.15" })
    @DisplayName("Aplica el descuento por volumen según los umbrales")
    void debeAplicarDescuentoPorVolumen(int personas, double esperado) {
        assertEquals(esperado, reservaService.calcularDescuento(personas), 0.0001);
    }

    @Test
    @DisplayName("Rechaza un número de personas no positivo")
    void debeRechazarPersonasInvalidas() {
        assertThrows(IllegalArgumentException.class,
                () -> reservaService.calcularDescuento(0));
    }

    @Test
    @DisplayName("Cotiza aplicando descuento sobre el subtotal")
    void debeCotizarConDescuento() {
        when(actividadRepository.buscarPorId(1L))
                .thenReturn(Optional.of(new Actividad(1L, "City tour", 100_000, 20)));

        // 6 personas x 100.000 = 600.000, con 10% -> 540.000
        assertEquals(500_000.0, reservaService.cotizar(1L, 6), 0.01);
        verify(actividadRepository).buscarPorId(1L);
    }

    @Test
    @DisplayName("Falla si la actividad no existe")
    void debeFallarSiNoExisteLaActividad() {
        when(actividadRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(ActividadNoEncontradaException.class,
                () -> reservaService.cotizar(99L, 2));
    }

    @Test
    @DisplayName("Falla si se solicita más cupo del disponible")
    void debeFallarPorCupoInsuficiente() {
        when(actividadRepository.buscarPorId(3L))
                .thenReturn(Optional.of(new Actividad(3L, "Totumo", 60_000, 2)));

        assertThrows(CupoInsuficienteException.class,
                () -> reservaService.cotizar(3L, 5));
    }
}
