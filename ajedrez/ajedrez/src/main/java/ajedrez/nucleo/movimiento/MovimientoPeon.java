package ajedrez.nucleo.movimiento;

import ajedrez.nucleo.Color;
import ajedrez.nucleo.Pieza;
import ajedrez.nucleo.Posicion;
import ajedrez.nucleo.Tablero;
import java.util.ArrayList;
import java.util.List;

/**
 * Avanza hacia adelante (una casilla, o dos desde su fila inicial) solo sobre casillas vacías,
 * y captura en diagonal hacia adelante. Las blancas avanzan hacia la fila 0.
 * La fila inicial se deduce del tamaño del tablero, por lo que funciona con cualquier dimensión.
 * Alcance actual: sin captura al paso ni promoción (son alcance opcional).
 */
public class MovimientoPeon implements EstrategiaMovimiento {

    @Override
    public List<Posicion> destinosPosibles(Pieza pieza, Posicion origen, Tablero tablero) {
        List<Posicion> destinos = new ArrayList<>();
        int sentido = sentido(pieza.obtenerColor());

        Posicion unPaso = origen.desplazar(sentido, 0);
        if (tablero.estaDentro(unPaso) && tablero.estaVacia(unPaso)) {
            destinos.add(unPaso);
            Posicion dosPasos = origen.desplazar(2 * sentido, 0);
            boolean enFilaInicial = origen.obtenerFila() == filaInicial(pieza.obtenerColor(), tablero);
            if (enFilaInicial && tablero.estaDentro(dosPasos) && tablero.estaVacia(dosPasos)) {
                destinos.add(dosPasos);
            }
        }

        for (int deltaColumna : new int[] {-1, 1}) {
            Posicion diagonal = origen.desplazar(sentido, deltaColumna);
            if (tablero.estaDentro(diagonal) && !tablero.estaVacia(diagonal)
                    && tablero.obtenerPieza(diagonal).obtenerColor() != pieza.obtenerColor()) {
                destinos.add(diagonal);
            }
        }
        return destinos;
    }

    /** El peón amenaza sus dos diagonales de avance, haya o no una pieza en ellas. */
    @Override
    public boolean ataca(Pieza pieza, Posicion origen, Posicion destino, Tablero tablero) {
        int sentido = sentido(pieza.obtenerColor());
        return destino.obtenerFila() == origen.obtenerFila() + sentido
                && Math.abs(destino.obtenerColumna() - origen.obtenerColumna()) == 1;
    }

    private int sentido(Color color) {
        return color == Color.BLANCO ? -1 : 1;
    }

    private int filaInicial(Color color, Tablero tablero) {
        return color == Color.BLANCO ? tablero.obtenerFilas() - 2 : 1;
    }
}
