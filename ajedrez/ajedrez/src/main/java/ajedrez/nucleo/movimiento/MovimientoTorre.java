package ajedrez.nucleo.movimiento;

import ajedrez.nucleo.Pieza;
import ajedrez.nucleo.Posicion;
import ajedrez.nucleo.Tablero;
import java.util.List;

/** Se desliza en línea recta por filas y columnas sin saltar piezas. */
public class MovimientoTorre implements EstrategiaMovimiento {

    @Override
    public List<Posicion> destinosPosibles(Pieza pieza, Posicion origen, Tablero tablero) {
        return Recorridos.sinPiezasPropias(pieza, tablero,
                Recorridos.porLineas(origen, tablero, Recorridos.ORTOGONALES));
    }

    @Override
    public boolean ataca(Pieza pieza, Posicion origen, Posicion destino, Tablero tablero) {
        return Recorridos.esOrtogonal(origen, destino) && tablero.caminoLibre(origen, destino);
    }
}
