package ajedrez.controlador;

import static org.junit.jupiter.api.Assertions.*;

import ajedrez.nucleo.Color;
import ajedrez.nucleo.FabricaPartidas;
import ajedrez.nucleo.Partida;
import ajedrez.nucleo.Posicion;
import ajedrez.vista.VistaAjedrez;
import ajedrez.vista.consola.NotacionAlgebraica;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** El controlador se prueba con una vista falsa: no hace falta consola ni GUI. */
class ControladorAjedrezTest {

    private static class VistaEspia implements VistaAjedrez {
        final List<String> eventos = new ArrayList<>();

        @Override
        public void mostrarTablero(Partida partida) {
            eventos.add("tablero");
        }

        @Override
        public void mostrarTurno(Color color) {
            eventos.add("turno:" + color);
        }

        @Override
        public void mostrarMensaje(String mensaje) {
            eventos.add("mensaje:" + mensaje);
        }

        @Override
        public void mostrarJaque(Color color) {
            eventos.add("jaque:" + color);
        }
    }

    private VistaEspia vista;
    private ControladorAjedrez controlador;

    private static Posicion p(String casilla) {
        return NotacionAlgebraica.parsear(casilla, 8, 8);
    }

    @BeforeEach
    void preparar() {
        vista = new VistaEspia();
        controlador = new ControladorAjedrez(FabricaPartidas.partidaEstandar("A", "B"), vista);
    }

    @Test
    void iniciarMuestraTableroYTurno() {
        controlador.iniciar();
        assertTrue(vista.eventos.contains("tablero"));
        assertTrue(vista.eventos.contains("turno:BLANCO"));
    }

    @Test
    void unMovimientoValidoActualizaLaVista() {
        controlador.solicitarMovimiento(p("e2"), p("e4"));
        assertEquals(List.of("tablero", "turno:NEGRO"), vista.eventos);
    }

    @Test
    void unMovimientoInvalidoSoloMuestraUnMensaje() {
        controlador.solicitarMovimiento(p("e2"), p("e5"));
        assertEquals(1, vista.eventos.size());
        assertTrue(vista.eventos.get(0).startsWith("mensaje:Movimiento inválido"));
    }

    @Test
    void unJaqueSeNotificaAlJugadorQueDebeResponder() {
        controlador.solicitarMovimiento(p("f2"), p("f3"));
        controlador.solicitarMovimiento(p("e7"), p("e5"));
        controlador.solicitarMovimiento(p("g2"), p("g4"));
        vista.eventos.clear();
        controlador.solicitarMovimiento(p("d8"), p("h4"));
        assertEquals("jaque:BLANCO", vista.eventos.get(vista.eventos.size() - 1));
    }

    @Test
    void unaCapturaSeAnuncia() {
        controlador.solicitarMovimiento(p("e2"), p("e4"));
        controlador.solicitarMovimiento(p("d7"), p("d5"));
        vista.eventos.clear();
        controlador.solicitarMovimiento(p("e4"), p("d5"));
        assertTrue(vista.eventos.get(0).startsWith("mensaje:Captura"));
    }

    @Test
    void noAceptaDependenciasNulas() {
        assertThrows(NullPointerException.class, () -> new ControladorAjedrez(null, vista));
    }
}
