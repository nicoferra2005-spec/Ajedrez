package ajedrez.nucleo.movimiento;

import static ajedrez.nucleo.Escenarios.*;
import static org.junit.jupiter.api.Assertions.*;

import ajedrez.nucleo.Color;
import ajedrez.nucleo.Pieza;
import ajedrez.nucleo.Posicion;
import ajedrez.nucleo.Tablero;
import ajedrez.nucleo.TipoPieza;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class MovimientoPiezasTest {

    private static Set<Posicion> destinos(Tablero tablero, String origen) {
        Posicion desde = pos(origen);
        return new HashSet<>(tablero.obtenerPieza(desde).destinosPosibles(desde, tablero));
    }

    @Nested
    class Peon {
        @Test
        void blancoAvanzaUnoODosDesdeSuFilaInicial() {
            Tablero t = vacio();
            poner(t, "e2", TipoPieza.PEON, Color.BLANCO);
            assertEquals(posiciones("e3", "e4"), destinos(t, "e2"));
        }

        @Test
        void negroAvanzaHaciaAbajo() {
            Tablero t = vacio();
            poner(t, "d7", TipoPieza.PEON, Color.NEGRO);
            assertEquals(posiciones("d6", "d5"), destinos(t, "d7"));
        }

        @Test
        void fueraDeSuFilaInicialSoloAvanzaUno() {
            Tablero t = vacio();
            poner(t, "e3", TipoPieza.PEON, Color.BLANCO);
            assertEquals(posiciones("e4"), destinos(t, "e3"));
        }

        @Test
        void noAvanzaSiHayUnaPiezaDelanteNiSalta() {
            Tablero t = vacio();
            poner(t, "e2", TipoPieza.PEON, Color.BLANCO);
            poner(t, "e3", TipoPieza.PEON, Color.NEGRO);
            assertEquals(posiciones(), destinos(t, "e2"));
        }

        @Test
        void noPuedeAvanzarDosSiLaCasillaFinalEstaOcupada() {
            Tablero t = vacio();
            poner(t, "e2", TipoPieza.PEON, Color.BLANCO);
            poner(t, "e4", TipoPieza.CABALLO, Color.NEGRO);
            assertEquals(posiciones("e3"), destinos(t, "e2"));
        }

        @Test
        void capturaEnDiagonalSoloPiezasRivales() {
            Tablero t = vacio();
            poner(t, "e4", TipoPieza.PEON, Color.BLANCO);
            poner(t, "d5", TipoPieza.TORRE, Color.NEGRO);
            poner(t, "f5", TipoPieza.TORRE, Color.BLANCO);
            assertEquals(posiciones("e5", "d5"), destinos(t, "e4"));
        }

        @Test
        void ataqueEsDiagonalAunqueLaCasillaEsteVacia() {
            Tablero t = vacio();
            poner(t, "e4", TipoPieza.PEON, Color.BLANCO);
            Pieza peon = t.obtenerPieza(pos("e4"));
            assertTrue(peon.ataca(pos("e4"), pos("d5"), t));
            assertTrue(peon.ataca(pos("e4"), pos("f5"), t));
            assertFalse(peon.ataca(pos("e4"), pos("e5"), t));
            assertFalse(peon.ataca(pos("e4"), pos("d3"), t), "no ataca hacia atrás");
        }
    }

    @Nested
    class Torre {
        @Test
        void sinObstaculosCubreFilaYColumna() {
            Tablero t = vacio();
            poner(t, "d4", TipoPieza.TORRE, Color.BLANCO);
            assertEquals(14, destinos(t, "d4").size());
        }

        @Test
        void seDetieneAntesDeUnaPiezaPropiaYEnLaRival() {
            Tablero t = vacio();
            poner(t, "a1", TipoPieza.TORRE, Color.BLANCO);
            poner(t, "a3", TipoPieza.PEON, Color.BLANCO);
            poner(t, "c1", TipoPieza.PEON, Color.NEGRO);
            assertEquals(posiciones("a2", "b1", "c1"), destinos(t, "a1"));
        }

        @Test
        void noAtacaEnDiagonalNiPorEncimaDeOtraPieza() {
            Tablero t = vacio();
            poner(t, "a1", TipoPieza.TORRE, Color.BLANCO);
            poner(t, "a4", TipoPieza.PEON, Color.BLANCO);
            Pieza torre = t.obtenerPieza(pos("a1"));
            assertFalse(torre.ataca(pos("a1"), pos("b2"), t));
            assertFalse(torre.ataca(pos("a1"), pos("a8"), t));
            assertTrue(torre.ataca(pos("a1"), pos("a4"), t));
        }
    }

    @Nested
    class Alfil {
        @Test
        void soloSeMueveEnDiagonal() {
            Tablero t = vacio();
            poner(t, "c1", TipoPieza.ALFIL, Color.BLANCO);
            assertEquals(posiciones("b2", "a3", "d2", "e3", "f4", "g5", "h6"), destinos(t, "c1"));
        }

        @Test
        void lasPiezasBloqueanElCamino() {
            Tablero t = vacio();
            poner(t, "c1", TipoPieza.ALFIL, Color.BLANCO);
            poner(t, "d2", TipoPieza.PEON, Color.BLANCO);
            poner(t, "b2", TipoPieza.PEON, Color.NEGRO);
            assertEquals(posiciones("b2"), destinos(t, "c1"));
        }
    }

    @Nested
    class Reina {
        @Test
        void combinaTorreYAlfil() {
            Tablero t = vacio();
            poner(t, "d4", TipoPieza.REINA, Color.BLANCO);
            assertEquals(27, destinos(t, "d4").size());
        }

        @Test
        void ataqueRespetaObstaculos() {
            Tablero t = vacio();
            poner(t, "d1", TipoPieza.REINA, Color.BLANCO);
            poner(t, "d3", TipoPieza.PEON, Color.BLANCO);
            Pieza reina = t.obtenerPieza(pos("d1"));
            assertFalse(reina.ataca(pos("d1"), pos("d5"), t));
            assertTrue(reina.ataca(pos("d1"), pos("h5"), t));
        }
    }

    @Nested
    class Caballo {
        @Test
        void saltaSobreOtrasPiezas() {
            Tablero t = vacio();
            poner(t, "b1", TipoPieza.CABALLO, Color.BLANCO);
            poner(t, "b2", TipoPieza.PEON, Color.BLANCO);
            poner(t, "c2", TipoPieza.PEON, Color.BLANCO);
            poner(t, "a2", TipoPieza.PEON, Color.BLANCO);
            assertEquals(posiciones("a3", "c3", "d2"), destinos(t, "b1"));
        }

        @Test
        void enElCentroTieneOchoDestinos() {
            Tablero t = vacio();
            poner(t, "d4", TipoPieza.CABALLO, Color.NEGRO);
            assertEquals(8, destinos(t, "d4").size());
        }

        @Test
        void noCapturaPiezasPropias() {
            Tablero t = vacio();
            poner(t, "b1", TipoPieza.CABALLO, Color.BLANCO);
            poner(t, "c3", TipoPieza.PEON, Color.BLANCO);
            poner(t, "a3", TipoPieza.PEON, Color.NEGRO);
            assertEquals(posiciones("a3", "d2"), destinos(t, "b1"));
        }
    }

    @Nested
    class Rey {
        @Test
        void seMueveUnaCasillaEnCualquierDireccion() {
            Tablero t = vacio();
            poner(t, "e4", TipoPieza.REY, Color.BLANCO);
            assertEquals(posiciones("d3", "e3", "f3", "d4", "f4", "d5", "e5", "f5"), destinos(t, "e4"));
        }

        @Test
        void enLaEsquinaTieneTresDestinos() {
            Tablero t = vacio();
            poner(t, "a1", TipoPieza.REY, Color.BLANCO);
            assertEquals(posiciones("a2", "b1", "b2"), destinos(t, "a1"));
        }
    }
}
