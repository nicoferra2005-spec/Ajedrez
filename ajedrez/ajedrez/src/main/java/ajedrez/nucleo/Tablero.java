package ajedrez.nucleo;

import java.util.ArrayList;
import java.util.List;

/**
 * Grilla de casillas que guarda las piezas. Solo conoce geometría y ocupación;
 * las reglas del juego viven en {@link ReglasAjedrez} y los movimientos en las estrategias.
 * El tamaño es parametrizable (no está fijo en 8x8).
 */
public class Tablero {

    private final int filas;
    private final int columnas;
    private final Pieza[][] casillas;

    public Tablero(int filas, int columnas) {
        if (filas <= 0 || columnas <= 0) {
            throw new IllegalArgumentException("El tablero necesita al menos una fila y una columna.");
        }
        this.filas = filas;
        this.columnas = columnas;
        this.casillas = new Pieza[filas][columnas];
    }

    public int obtenerFilas() {
        return filas;
    }

    public int obtenerColumnas() {
        return columnas;
    }

    public boolean estaDentro(Posicion posicion) {
        return posicion.obtenerFila() >= 0 && posicion.obtenerFila() < filas
                && posicion.obtenerColumna() >= 0 && posicion.obtenerColumna() < columnas;
    }

    /** @return la pieza en la casilla, o {@code null} si está vacía. */
    public Pieza obtenerPieza(Posicion posicion) {
        validarDentro(posicion);
        return casillas[posicion.obtenerFila()][posicion.obtenerColumna()];
    }

    public boolean estaVacia(Posicion posicion) {
        return obtenerPieza(posicion) == null;
    }

    /** Coloca la pieza, reemplazando lo que hubiera en la casilla. */
    public void colocarPieza(Posicion posicion, Pieza pieza) {
        validarDentro(posicion);
        casillas[posicion.obtenerFila()][posicion.obtenerColumna()] = pieza;
    }

    /** @return la pieza retirada, o {@code null} si la casilla estaba vacía. */
    public Pieza retirarPieza(Posicion posicion) {
        validarDentro(posicion);
        Pieza retirada = casillas[posicion.obtenerFila()][posicion.obtenerColumna()];
        casillas[posicion.obtenerFila()][posicion.obtenerColumna()] = null;
        return retirada;
    }

    /** @return la posición del rey del color pedido, o {@code null} si no hay ninguno. */
    public Posicion buscarRey(Color color) {
        for (Posicion posicion : obtenerPosicionesOcupadas(color)) {
            if (obtenerPieza(posicion).obtenerTipo() == TipoPieza.REY) {
                return posicion;
            }
        }
        return null;
    }

    public List<Posicion> obtenerPosicionesOcupadas(Color color) {
        List<Posicion> ocupadas = new ArrayList<>();
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                Pieza pieza = casillas[fila][columna];
                if (pieza != null && pieza.obtenerColor() == color) {
                    ocupadas.add(new Posicion(fila, columna));
                }
            }
        }
        return ocupadas;
    }

    /**
     * @return true si origen y destino están alineados (fila, columna o diagonal), ambos
     *         dentro del tablero, y todas las casillas intermedias están vacías.
     */
    public boolean caminoLibre(Posicion origen, Posicion destino) {
        if (!estaDentro(origen) || !estaDentro(destino) || origen.equals(destino)) {
            return false;
        }
        int deltaFila = destino.obtenerFila() - origen.obtenerFila();
        int deltaColumna = destino.obtenerColumna() - origen.obtenerColumna();
        boolean alineados = deltaFila == 0 || deltaColumna == 0
                || Math.abs(deltaFila) == Math.abs(deltaColumna);
        if (!alineados) {
            return false;
        }
        int pasoFila = Integer.signum(deltaFila);
        int pasoColumna = Integer.signum(deltaColumna);
        Posicion actual = origen.desplazar(pasoFila, pasoColumna);
        while (!actual.equals(destino)) {
            if (!estaVacia(actual)) {
                return false;
            }
            actual = actual.desplazar(pasoFila, pasoColumna);
        }
        return true;
    }

    /** Copia independiente: mover piezas en la copia no afecta al original. */
    public Tablero copiar() {
        Tablero copia = new Tablero(filas, columnas);
        for (int fila = 0; fila < filas; fila++) {
            System.arraycopy(casillas[fila], 0, copia.casillas[fila], 0, columnas);
        }
        return copia;
    }

    /**
     * Mueve la pieza de {@code origen} a {@code destino} sin validar reglas.
     *
     * @return la pieza capturada, o {@code null} si el destino estaba vacío.
     */
    public Pieza aplicarMovimiento(Posicion origen, Posicion destino) {
        Pieza movida = retirarPieza(origen);
        if (movida == null) {
            throw new IllegalArgumentException("No hay pieza en " + origen);
        }
        Pieza capturada = retirarPieza(destino);
        colocarPieza(destino, movida);
        return capturada;
    }

    private void validarDentro(Posicion posicion) {
        if (!estaDentro(posicion)) {
            throw new IllegalArgumentException("Posición fuera del tablero: " + posicion);
        }
    }
}
