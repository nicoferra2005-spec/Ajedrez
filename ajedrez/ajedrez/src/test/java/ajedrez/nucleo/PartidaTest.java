package ajedrez.nucleo;

import static ajedrez.nucleo.Escenarios.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartidaTest {

    private Partida partida;

    @BeforeEach
    void iniciar() {
        partida = FabricaPartidas.partidaEstandar("Ana", "Beto");
    }

    private ResultadoMovimiento mover(String origen, String destino) {
        return partida.intentarMover(pos(origen), pos(destino));
    }

    @Test
    void empiezanLasBlancasEnUnTablero8x8ConTreintaYDosPiezas() {
        assertEquals(Color.BLANCO, partida.obtenerTurno());
        assertEquals("Ana", partida.obtenerJugadorActual().obtenerNombre());
        assertEquals(8, partida.obtenerFilas());
        assertEquals(8, partida.obtenerColumnas());
        int piezas = 0;
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                if (partida.obtenerPieza(new Posicion(f, c)) != null) {
                    piezas++;
                }
            }
        }
        assertEquals(32, piezas);
    }

    @Test
    void posicionInicialCorrecta() {
        assertEquals(TipoPieza.REINA, partida.obtenerPieza(pos("d1")).obtenerTipo());
        assertEquals(TipoPieza.REY, partida.obtenerPieza(pos("e1")).obtenerTipo());
        assertEquals(Color.BLANCO, partida.obtenerPieza(pos("e1")).obtenerColor());
        assertEquals(TipoPieza.REINA, partida.obtenerPieza(pos("d8")).obtenerTipo());
        assertEquals(Color.NEGRO, partida.obtenerPieza(pos("a8")).obtenerColor());
    }

    @Test
    void losTurnosSeAlternanSoloConMovimientosAceptados() {
        assertTrue(mover("e2", "e4").fueAceptado());
        assertEquals(Color.NEGRO, partida.obtenerTurno());
        assertTrue(mover("e7", "e5").fueAceptado());
        assertEquals(Color.BLANCO, partida.obtenerTurno());
    }

    @Test
    void unMovimientoInvalidoNoCambiaElTurnoNiElTablero() {
        ResultadoMovimiento resultado = mover("e2", "e5");
        assertFalse(resultado.fueAceptado());
        assertFalse(resultado.obtenerMensaje().isBlank());
        assertEquals(Color.BLANCO, partida.obtenerTurno());
        assertNotNull(partida.obtenerPieza(pos("e2")));
        assertNull(partida.obtenerPieza(pos("e5")));
    }

    @Test
    void noSePuedeMoverUnaPiezaDelRival() {
        assertFalse(mover("e7", "e5").fueAceptado());
    }

    @Test
    void noSePuedeMoverDesdeUnaCasillaVacia() {
        assertFalse(mover("e4", "e5").fueAceptado());
    }

    @Test
    void elCaballoSaltaSobreSusPropiasPiezas() {
        assertTrue(mover("g1", "f3").fueAceptado());
    }

    @Test
    void laTorreNoPuedeSaltarSobreLosPeones() {
        assertFalse(mover("a1", "a4").fueAceptado());
    }

    @Test
    void noSePuedeCapturarUnaPiezaPropia() {
        assertFalse(mover("d1", "d2").fueAceptado());
    }

    @Test
    void laCapturaSeInformaYSacaLaPiezaDelTablero() {
        mover("e2", "e4");
        mover("d7", "d5");
        ResultadoMovimiento captura = mover("e4", "d5");
        assertTrue(captura.fueAceptado());
        assertTrue(captura.hayCaptura());
        assertEquals(TipoPieza.PEON, captura.obtenerCaptura().obtenerTipo());
        assertEquals(Color.NEGRO, captura.obtenerCaptura().obtenerColor());
        assertEquals(Color.BLANCO, partida.obtenerPieza(pos("d5")).obtenerColor());
    }

    @Test
    void unMovimientoSinCapturaNoInformaCaptura() {
        ResultadoMovimiento r = mover("e2", "e4");
        assertFalse(r.hayCaptura());
        assertNull(r.obtenerCaptura());
    }

    @Test
    void sePuedeConsultarLosMovimientosLegalesDeUnaPieza() {
        assertEquals(posiciones("f3", "h3"), new java.util.HashSet<>(partida.obtenerMovimientosLegales(pos("g1"))));
        assertTrue(partida.obtenerMovimientosLegales(pos("a1")).isEmpty());
    }

    @Test
    void detectaElJaqueEnElMateDelLoco() {
        // 1.f3 e5 2.g4 Dh4#
        mover("f2", "f3");
        mover("e7", "e5");
        mover("g2", "g4");
        ResultadoMovimiento jaque = mover("d8", "h4");
        assertTrue(jaque.fueAceptado());
        assertTrue(jaque.hayJaque());
        assertTrue(partida.estaEnJaque(Color.BLANCO));
        assertFalse(partida.estaEnJaque(Color.NEGRO));
    }

    @Test
    void unMovimientoNormalNoReportaJaque() {
        assertFalse(mover("e2", "e4").hayJaque());
    }

    @Test
    void noSePuedeDejarAlPropioReyEnJaque() {
        mover("e2", "e4");
        mover("e7", "e5");
        mover("d1", "h5");
        mover("a7", "a6");
        mover("f1", "c4");
        mover("d8", "f6");
        mover("c4", "f7"); // jaque al rey negro: hay que responder
        assertTrue(partida.estaEnJaque(Color.NEGRO));
        ResultadoMovimiento ignora = mover("a6", "a5");
        assertFalse(ignora.fueAceptado());
        assertEquals("Ese movimiento deja a tu rey en jaque.", ignora.obtenerMensaje());
    }

    @Test
    void losJugadoresDebenTenerSuColor() {
        assertThrows(IllegalArgumentException.class, () -> new Partida(
                vacio(), new Jugador("a", Color.NEGRO), new Jugador("b", Color.BLANCO), new ReglasAjedrez()));
    }

    @Test
    void unJugadorNecesitaNombre() {
        assertThrows(IllegalArgumentException.class, () -> new Jugador(" ", Color.BLANCO));
    }
}
