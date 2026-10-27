package ajedrez.nucleo.movimiento;

import ajedrez.nucleo.Pieza;
import ajedrez.nucleo.Posicion;
import ajedrez.nucleo.Tablero;
import java.util.List;

/** Se desliza en diagonal sin saltar piezas. */
public class MovimientoAlfil implements EstrategiaMovimiento {

    @Override
    public List<Posicion> destinosPosibles(Pieza pieza, Posicion origen, Tablero tablero) {
        return Recorridos.sinPiezasPropias(pieza, tablero,
                Recorridos.porLineas(origen, tablero, Recorridos.DIAGONALES));
    }

    @Override
    public boolean ataca(Pieza pieza, Posicion origen, Posicion destino, Tablero tablero) {
        return Recorridos.esDiagonal(origen, destino) && tablero.caminoLibre(origen, destino);
    }
}
