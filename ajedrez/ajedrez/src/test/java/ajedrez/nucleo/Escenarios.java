package ajedrez.nucleo;

import java.util.Set;
import java.util.stream.Collectors;

/** Helpers de test: arman tableros chicos con notación "e4" sin depender de la vista. */
public final class Escenarios {

    public static final FabricaPiezas FABRICA = new FabricaPiezas();

    private Escenarios() {
    }

    public static Posicion pos(String casilla) {
        return pos(casilla, 8);
    }

    public static Posicion pos(String casilla, int filas) {
        return new Posicion(filas - Integer.parseInt(casilla.substring(1)), casilla.charAt(0) - 'a');
    }

    public static Tablero vacio() {
        return new Tablero(8, 8);
    }

    public static void poner(Tablero tablero, String casilla, TipoPieza tipo, Color color) {
        tablero.colocarPieza(pos(casilla, tablero.obtenerFilas()), FABRICA.crear(tipo, color));
    }

    public static Set<Posicion> posiciones(String... casillas) {
        return posiciones(8, casillas);
    }

    public static Set<Posicion> posiciones(int filas, String... casillas) {
        return java.util.Arrays.stream(casillas).map(c -> pos(c, filas)).collect(Collectors.toSet());
    }
}
