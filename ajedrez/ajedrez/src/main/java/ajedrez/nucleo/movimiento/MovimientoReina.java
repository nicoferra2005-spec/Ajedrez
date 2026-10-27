package ajedrez.nucleo.movimiento;

import ajedrez.nucleo.Pieza;
import ajedrez.nucleo.Posicion;
import ajedrez.nucleo.Tablero;
import java.util.List;

/** Combina el recorrido de torre y alfil. */
public class MovimientoReina implements EstrategiaMovimiento {

    @Override
    public List<Posicion> destinosPosibles(Pieza pieza, Posicion origen, Tablero tablero) {
        return Recorridos.sinPiezasPropias(pieza, tablero,
                Recorridos.porLineas(origen, tablero, Recorridos.OCHO_DIRECCIONES));
    }

    @Override
    public boolean ataca(Pieza pieza, Posicion origen, Posicion destino, Tablero tablero) {
        boolean alineados = Recorridos.esOrtogonal(origen, destino) || Recorridos.esDiagonal(origen, destino);
        return alineados && tablero.caminoLibre(origen, destino);
    }
}
