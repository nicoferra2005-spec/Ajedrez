package ajedrez.vista.consola;

import ajedrez.nucleo.Posicion;

/**
 * Traduce entre el texto que escribe el usuario ("e2") y las {@link Posicion} del núcleo.
 * Columnas: a, b, c... Filas: numeradas desde abajo (la fila 1 es la de las blancas).
 * Se adapta al tamaño del tablero.
 */
public final class NotacionAlgebraica {

    private NotacionAlgebraica() {
    }

    public static Posicion parsear(String texto, int filas, int columnas) {
        String limpio = texto == null ? "" : texto.trim().toLowerCase();
        if (limpio.length() < 2) {
            throw new IllegalArgumentException("Casilla inválida: '" + texto + "'. Ejemplo: e2");
        }
        int columna = limpio.charAt(0) - 'a';
        int numeroFila;
        try {
            numeroFila = Integer.parseInt(limpio.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Casilla inválida: '" + texto + "'. Ejemplo: e2");
        }
        int fila = filas - numeroFila;
        if (columna < 0 || columna >= columnas || fila < 0 || fila >= filas) {
            throw new IllegalArgumentException("La casilla '" + texto + "' está fuera del tablero.");
        }
        return new Posicion(fila, columna);
    }

    public static String formatear(Posicion posicion, int filas) {
        return etiquetaColumna(posicion.obtenerColumna()) + etiquetaFila(posicion.obtenerFila(), filas);
    }

    public static String etiquetaColumna(int columna) {
        return String.valueOf((char) ('a' + columna));
    }

    public static String etiquetaFila(int fila, int filas) {
        return String.valueOf(filas - fila);
    }
}
