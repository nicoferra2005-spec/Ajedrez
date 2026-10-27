package ajedrez.nucleo.movimiento;

import ajedrez.nucleo.Pieza;
import ajedrez.nucleo.Posicion;
import ajedrez.nucleo.Tablero;
import java.util.List;

/** Salta en "L" (2+1); no se ve afectado por las piezas intermedias. */
public class MovimientoCaballo implements EstrategiaMovimiento {

    @Override
    public List<Posicion> destinosPosibles(Pieza pieza, Posicion origen, Tablero tablero) {
        return Recorridos.sinPiezasPropias(pieza, tablero,
                Recorridos.porSaltos(origen, tablero, Recorridos.SALTOS_CABALLO));
    }

    @Override
    public boolean ataca(Pieza pieza, Posicion origen, Posicion destino, Tablero tablero) {
        int deltaFila = Math.abs(destino.obtenerFila() - origen.obtenerFila());
        int deltaColumna = Math.abs(destino.obtenerColumna() - origen.obtenerColumna());
        return deltaFila * deltaColumna == 2;
    }
}
