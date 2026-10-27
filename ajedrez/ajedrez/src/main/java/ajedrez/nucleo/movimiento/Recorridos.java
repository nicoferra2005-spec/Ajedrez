package ajedrez.nucleo.movimiento;

import ajedrez.nucleo.Pieza;
import ajedrez.nucleo.Posicion;
import ajedrez.nucleo.Tablero;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilidades puras compartidas por las estrategias (sin estado, sin herencia):
 * recorrer rayos, probar saltos fijos y filtrar casillas ocupadas por piezas propias.
 */
final class Recorridos {

    static final int[][] ORTOGONALES = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    static final int[][] DIAGONALES = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    static final int[][] OCHO_DIRECCIONES = {
        {1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };
    static final int[][] SALTOS_CABALLO = {
        {2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };

    private Recorridos() {
    }

    /** Avanza por cada dirección hasta salir del tablero o toparse con la primera pieza (inclusive). */
    static List<Posicion> porLineas(Posicion origen, Tablero tablero, int[][] direcciones) {
        List<Posicion> casillas = new ArrayList<>();
        for (int[] direccion : direcciones) {
            Posicion actual = origen.desplazar(direccion[0], direccion[1]);
            while (tablero.estaDentro(actual)) {
                casillas.add(actual);
                if (!tablero.estaVacia(actual)) {
                    break;
                }
                actual = actual.desplazar(direccion[0], direccion[1]);
            }
        }
        return casillas;
    }

    /** Casillas a saltos fijos que caen dentro del tablero (ocupadas o no). */
    static List<Posicion> porSaltos(Posicion origen, Tablero tablero, int[][] saltos) {
        List<Posicion> casillas = new ArrayList<>();
        for (int[] salto : saltos) {
            Posicion destino = origen.desplazar(salto[0], salto[1]);
            if (tablero.estaDentro(destino)) {
                casillas.add(destino);
            }
        }
        return casillas;
    }

    /** Descarta las casillas ocupadas por piezas del mismo color que {@code pieza}. */
    static List<Posicion> sinPiezasPropias(Pieza pieza, Tablero tablero, List<Posicion> casillas) {
        List<Posicion> libres = new ArrayList<>();
        for (Posicion casilla : casillas) {
            Pieza ocupante = tablero.obtenerPieza(casilla);
            if (ocupante == null || ocupante.obtenerColor() != pieza.obtenerColor()) {
                libres.add(casilla);
            }
        }
        return libres;
    }

    static boolean esOrtogonal(Posicion origen, Posicion destino) {
        int deltaFila = destino.obtenerFila() - origen.obtenerFila();
        int deltaColumna = destino.obtenerColumna() - origen.obtenerColumna();
        return (deltaFila == 0) != (deltaColumna == 0);
    }

    static boolean esDiagonal(Posicion origen, Posicion destino) {
        int deltaFila = Math.abs(destino.obtenerFila() - origen.obtenerFila());
        int deltaColumna = Math.abs(destino.obtenerColumna() - origen.obtenerColumna());
        return deltaFila != 0 && deltaFila == deltaColumna;
    }
}
