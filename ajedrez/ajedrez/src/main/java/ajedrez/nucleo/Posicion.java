package ajedrez.nucleo;

/**
 * Value object inmutable: una casilla identificada por fila y columna (base 0).
 * La fila 0 es la más alejada de las blancas (la octava, en un tablero de 8x8).
 * No valida límites: eso es responsabilidad del {@link Tablero}.
 */
public final class Posicion {

    private final int fila;
    private final int columna;

    public Posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int obtenerFila() {
        return fila;
    }

    public int obtenerColumna() {
        return columna;
    }

    /** @return una nueva posición desplazada; la original no cambia. */
    public Posicion desplazar(int deltaFila, int deltaColumna) {
        return new Posicion(fila + deltaFila, columna + deltaColumna);
    }

    @Override
    public boolean equals(Object otro) {
        if (this == otro) {
            return true;
        }
        if (!(otro instanceof Posicion)) {
            return false;
        }
        Posicion that = (Posicion) otro;
        return fila == that.fila && columna == that.columna;
    }

    @Override
    public int hashCode() {
        return 31 * fila + columna;
    }

    @Override
    public String toString() {
        return "(" + fila + "," + columna + ")";
    }
}
