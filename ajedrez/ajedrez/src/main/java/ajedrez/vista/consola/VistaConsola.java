package ajedrez.vista.consola;

import ajedrez.nucleo.Color;
import ajedrez.nucleo.Partida;
import ajedrez.nucleo.Pieza;
import ajedrez.nucleo.Posicion;
import ajedrez.nucleo.TipoPieza;
import ajedrez.vista.VistaAjedrez;
import java.io.PrintStream;
import java.util.EnumMap;
import java.util.Map;

/**
 * Vista de texto. Blancas en MAYÚSCULA y negras en minúscula:
 * P=Peón, T=Torre, C=Caballo, A=Alfil, D=Dama/Reina, R=Rey.
 */
public class VistaConsola implements VistaAjedrez {

    private static final Map<TipoPieza, Character> SIMBOLOS = new EnumMap<>(TipoPieza.class);

    static {
        SIMBOLOS.put(TipoPieza.PEON, 'P');
        SIMBOLOS.put(TipoPieza.TORRE, 'T');
        SIMBOLOS.put(TipoPieza.CABALLO, 'C');
        SIMBOLOS.put(TipoPieza.ALFIL, 'A');
        SIMBOLOS.put(TipoPieza.REINA, 'D');
        SIMBOLOS.put(TipoPieza.REY, 'R');
    }

    private final PrintStream salida;

    public VistaConsola(PrintStream salida) {
        this.salida = salida;
    }

    @Override
    public void mostrarTablero(Partida partida) {
        int filas = partida.obtenerFilas();
        int columnas = partida.obtenerColumnas();
        salida.println();
        for (int fila = 0; fila < filas; fila++) {
            StringBuilder linea = new StringBuilder(String.format("%2s |", NotacionAlgebraica.etiquetaFila(fila, filas)));
            for (int columna = 0; columna < columnas; columna++) {
                linea.append(' ').append(simbolo(partida.obtenerPieza(new Posicion(fila, columna))));
            }
            salida.println(linea);
        }
        StringBuilder pie = new StringBuilder("   +");
        StringBuilder letras = new StringBuilder("    ");
        for (int columna = 0; columna < columnas; columna++) {
            pie.append("--");
            letras.append(NotacionAlgebraica.etiquetaColumna(columna)).append(' ');
        }
        salida.println(pie);
        salida.println(letras);
    }

    @Override
    public void mostrarTurno(Color color) {
        salida.println("Turno de: " + color);
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        salida.println(mensaje);
    }

    @Override
    public void mostrarJaque(Color color) {
        salida.println("¡JAQUE! El rey " + color + " está en jaque.");
    }

    private char simbolo(Pieza pieza) {
        if (pieza == null) {
            return '.';
        }
        char simbolo = SIMBOLOS.getOrDefault(pieza.obtenerTipo(), '?');
        return pieza.obtenerColor() == Color.BLANCO ? simbolo : Character.toLowerCase(simbolo);
    }
}
