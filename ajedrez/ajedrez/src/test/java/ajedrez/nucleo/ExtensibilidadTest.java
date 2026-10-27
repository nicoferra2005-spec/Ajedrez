package ajedrez.nucleo;

import static ajedrez.nucleo.Escenarios.*;
import static org.junit.jupiter.api.Assertions.*;

import ajedrez.nucleo.movimiento.EstrategiaMovimiento;
import ajedrez.nucleo.movimiento.MovimientoCaballo;
import ajedrez.nucleo.movimiento.MovimientoReina;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Demuestra la escalabilidad: se agrega una pieza nueva y un tablero de otro tamaño
 * sin modificar ninguna clase del núcleo (solo se compone lo que ya existe).
 */
class ExtensibilidadTest {

    /** Pieza ficticia "Amazona": se mueve como reina y como caballo. */
    private static class MovimientoAmazona implements EstrategiaMovimiento {
        private final EstrategiaMovimiento reina = new MovimientoReina();
        private final EstrategiaMovimiento caballo = new MovimientoCaballo();

        @Override
        public List<Posicion> destinosPosibles(Pieza pieza, Posicion origen, Tablero tablero) {
            List<Posicion> todos = new ArrayList<>(reina.destinosPosibles(pieza, origen, tablero));
            todos.addAll(caballo.destinosPosibles(pieza, origen, tablero));
            return todos;
        }

        @Override
        public boolean ataca(Pieza pieza, Posicion origen, Posicion destino, Tablero tablero) {
            return reina.ataca(pieza, origen, destino, tablero) || caballo.ataca(pieza, origen, destino, tablero);
        }
    }

    @Test
    void unaPiezaNuevaConMovimientoPropioFuncionaEnLasReglas() {
        Tablero t = vacio();
        t.colocarPieza(pos("d4"), new Pieza(Color.BLANCO, TipoPieza.REINA, new MovimientoAmazona()));
        poner(t, "e6", TipoPieza.REY, Color.NEGRO); // a un salto de caballo de d4
        assertTrue(new ReglasAjedrez().estaEnJaque(t, Color.NEGRO));
        Set<Posicion> destinos = new HashSet<>(t.obtenerPieza(pos("d4")).destinosPosibles(pos("d4"), t));
        assertEquals(27 + 8, destinos.size());
    }

    @Test
    void sePuedeReemplazarElMovimientoDeUnTipoDesdeLaFabrica() {
        FabricaPiezas fabrica = new FabricaPiezas().registrar(TipoPieza.ALFIL, new MovimientoAmazona());
        Tablero t = vacio();
        t.colocarPieza(pos("a1"), fabrica.crear(TipoPieza.ALFIL, Color.BLANCO));
        assertTrue(t.obtenerPieza(pos("a1")).destinosPosibles(pos("a1"), t).contains(pos("a8")));
    }

    @Test
    void elTableroYLasReglasFuncionanConOtrosTamanios() {
        Tablero t = new Tablero(10, 12);
        t.colocarPieza(pos("a1", 10), FABRICA.crear(TipoPieza.TORRE, Color.BLANCO));
        t.colocarPieza(pos("l10", 10), FABRICA.crear(TipoPieza.REY, Color.NEGRO));
        assertTrue(t.estaDentro(pos("l10", 10)));
        assertFalse(t.estaDentro(new Posicion(10, 0)));
        assertEquals(9 + 11, t.obtenerPieza(pos("a1", 10)).destinosPosibles(pos("a1", 10), t).size());
        assertFalse(new ReglasAjedrez().estaEnJaque(t, Color.NEGRO));
        t.colocarPieza(pos("l1", 10), FABRICA.crear(TipoPieza.TORRE, Color.BLANCO));
        assertTrue(new ReglasAjedrez().estaEnJaque(t, Color.NEGRO));
    }

    @Test
    void elPeonSeAdaptaALaFilaInicialDeUnTableroMasGrande() {
        Tablero t = new Tablero(10, 10);
        t.colocarPieza(pos("c2", 10), FABRICA.crear(TipoPieza.PEON, Color.BLANCO));
        assertEquals(posiciones(10, "c3", "c4"),
                new HashSet<>(t.obtenerPieza(pos("c2", 10)).destinosPosibles(pos("c2", 10), t)));
    }
}
