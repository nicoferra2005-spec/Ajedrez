package ajedrez.vista;

import static org.junit.jupiter.api.Assertions.*;

import ajedrez.nucleo.Posicion;
import ajedrez.vista.consola.NotacionAlgebraica;
import org.junit.jupiter.api.Test;

class NotacionAlgebraicaTest {

    @Test
    void parseaCasillasDelTablero8x8() {
        assertEquals(new Posicion(7, 0), NotacionAlgebraica.parsear("a1", 8, 8));
        assertEquals(new Posicion(0, 7), NotacionAlgebraica.parsear("h8", 8, 8));
        assertEquals(new Posicion(4, 4), NotacionAlgebraica.parsear(" E4 ", 8, 8));
    }

    @Test
    void formatearEsLaInversaDeParsear() {
        assertEquals("e4", NotacionAlgebraica.formatear(new Posicion(4, 4), 8));
        assertEquals("l10", NotacionAlgebraica.formatear(NotacionAlgebraica.parsear("l10", 10, 12), 10));
    }

    @Test
    void rechazaTextoInvalidoOFueraDelTablero() {
        assertThrows(IllegalArgumentException.class, () -> NotacionAlgebraica.parsear("", 8, 8));
        assertThrows(IllegalArgumentException.class, () -> NotacionAlgebraica.parsear("e", 8, 8));
        assertThrows(IllegalArgumentException.class, () -> NotacionAlgebraica.parsear("ex", 8, 8));
        assertThrows(IllegalArgumentException.class, () -> NotacionAlgebraica.parsear("i1", 8, 8));
        assertThrows(IllegalArgumentException.class, () -> NotacionAlgebraica.parsear("a9", 8, 8));
        assertThrows(IllegalArgumentException.class, () -> NotacionAlgebraica.parsear("a0", 8, 8));
    }
}
