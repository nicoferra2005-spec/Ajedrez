package ajedrez.nucleo;

import static ajedrez.nucleo.Escenarios.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class TableroTest {

    @Test
    void noSePuedeCrearUnTableroSinCasillas() {
        assertThrows(IllegalArgumentException.class, () -> new Tablero(0, 8));
        assertThrows(IllegalArgumentException.class, () -> new Tablero(8, -1));
    }

    @Test
    void estaDentroRespetaLosLimites() {
        Tablero tablero = new Tablero(8, 8);
        assertTrue(tablero.estaDentro(new Posicion(0, 0)));
        assertTrue(tablero.estaDentro(new Posicion(7, 7)));
        assertFalse(tablero.estaDentro(new Posicion(8, 0)));
        assertFalse(tablero.estaDentro(new Posicion(0, -1)));
    }

    @Test
    void colocarYRetirarPieza() {
        Tablero tablero = vacio();
        poner(tablero, "d4", TipoPieza.TORRE, Color.BLANCO);
        assertEquals(TipoPieza.TORRE, tablero.obtenerPieza(pos("d4")).obtenerTipo());
        assertEquals(TipoPieza.TORRE, tablero.retirarPieza(pos("d4")).obtenerTipo());
        assertTrue(tablero.estaVacia(pos("d4")));
        assertNull(tablero.retirarPieza(pos("d4")));
    }

    @Test
    void accederFueraDelTableroLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> vacio().obtenerPieza(new Posicion(8, 8)));
    }

    @Test
    void buscarReyYPosicionesOcupadas() {
        Tablero tablero = vacio();
        poner(tablero, "e1", TipoPieza.REY, Color.BLANCO);
        poner(tablero, "a1", TipoPieza.TORRE, Color.BLANCO);
        poner(tablero, "e8", TipoPieza.REY, Color.NEGRO);
        assertEquals(pos("e1"), tablero.buscarRey(Color.BLANCO));
        assertEquals(pos("e8"), tablero.buscarRey(Color.NEGRO));
        assertEquals(2, tablero.obtenerPosicionesOcupadas(Color.BLANCO).size());
        assertEquals(List.of(pos("e8")), tablero.obtenerPosicionesOcupadas(Color.NEGRO));
    }

    @Test
    void buscarReySinReyDevuelveNull() {
        assertNull(vacio().buscarRey(Color.BLANCO));
    }

    @Test
    void caminoLibreConsideraLasCasillasIntermedias() {
        Tablero tablero = vacio();
        assertTrue(tablero.caminoLibre(pos("a1"), pos("a8")));
        assertTrue(tablero.caminoLibre(pos("a1"), pos("h8")));
        poner(tablero, "a4", TipoPieza.PEON, Color.NEGRO);
        assertFalse(tablero.caminoLibre(pos("a1"), pos("a8")));
        assertTrue(tablero.caminoLibre(pos("a1"), pos("a4")), "el destino no cuenta como obstáculo");
    }

    @Test
    void caminoLibreExigeAlineacion() {
        assertFalse(vacio().caminoLibre(pos("a1"), pos("b3")));
        assertFalse(vacio().caminoLibre(pos("a1"), pos("a1")));
    }

    @Test
    void copiarGeneraUnTableroIndependiente() {
        Tablero original = vacio();
        poner(original, "a1", TipoPieza.TORRE, Color.BLANCO);
        Tablero copia = original.copiar();
        copia.aplicarMovimiento(pos("a1"), pos("a5"));
        assertNotNull(original.obtenerPieza(pos("a1")));
        assertTrue(original.estaVacia(pos("a5")));
        assertNotNull(copia.obtenerPieza(pos("a5")));
    }

    @Test
    void aplicarMovimientoDevuelveLaPiezaCapturada() {
        Tablero tablero = vacio();
        poner(tablero, "a1", TipoPieza.TORRE, Color.BLANCO);
        poner(tablero, "a5", TipoPieza.CABALLO, Color.NEGRO);
        Pieza capturada = tablero.aplicarMovimiento(pos("a1"), pos("a5"));
        assertEquals(TipoPieza.CABALLO, capturada.obtenerTipo());
        assertEquals(Color.BLANCO, tablero.obtenerPieza(pos("a5")).obtenerColor());
        assertNull(tablero.aplicarMovimiento(pos("a5"), pos("a6")));
    }

    @Test
    void aplicarMovimientoDesdeCasillaVaciaFalla() {
        assertThrows(IllegalArgumentException.class, () -> vacio().aplicarMovimiento(pos("a1"), pos("a2")));
    }
}
