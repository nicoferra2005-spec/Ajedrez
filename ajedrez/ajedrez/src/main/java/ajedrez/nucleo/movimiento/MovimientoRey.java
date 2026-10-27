package ajedrez.nucleo.movimiento;

import ajedrez.nucleo.Pieza;
import ajedrez.nucleo.Posicion;
import ajedrez.nucleo.Tablero;
import java.util.List;

/**
 * Una casilla en cualquier dirección. Este patrón no evalúa si la casilla está amenazada:
 * esa validación es de {@code ReglasAjedrez}, que simula el movimiento y verifica el jaque.
 */
public class MovimientoRey implements EstrategiaMovimiento {

    @Override
    public List<Posicion> destinosPosibles(Pieza pieza, Posicion origen, Tablero tablero) {
        return Recorridos.sinPiezasPropias(pieza, tablero,
                Recorridos.porSaltos(origen, tablero, Recorridos.OCHO_DIRECCIONES));
    }

    @Override
    public boolean ataca(Pieza pieza, Posicion origen, Posicion destino, Tablero tablero) {
        int deltaFila = Math.abs(destino.obtenerFila() - origen.obtenerFila());
        int deltaColumna = Math.abs(destino.obtenerColumna() - origen.obtenerColumna());
        return Math.max(deltaFila, deltaColumna) == 1;
    }
}
