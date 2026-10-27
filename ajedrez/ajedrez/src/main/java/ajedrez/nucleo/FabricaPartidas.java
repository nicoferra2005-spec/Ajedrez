package ajedrez.nucleo;

/** Arma la configuración inicial del ajedrez clásico (tablero 8x8 con las 32 piezas). */
public final class FabricaPartidas {

    private static final int TAMANIO = 8;
    private static final TipoPieza[] FILA_TRASERA = {
        TipoPieza.TORRE, TipoPieza.CABALLO, TipoPieza.ALFIL, TipoPieza.REINA,
        TipoPieza.REY, TipoPieza.ALFIL, TipoPieza.CABALLO, TipoPieza.TORRE
    };

    private FabricaPartidas() {
    }

    public static Partida partidaEstandar(String nombreBlancas, String nombreNegras) {
        return new Partida(
                tableroEstandar(new FabricaPiezas()),
                new Jugador(nombreBlancas, Color.BLANCO),
                new Jugador(nombreNegras, Color.NEGRO),
                new ReglasAjedrez());
    }

    public static Tablero tableroEstandar(FabricaPiezas fabrica) {
        Tablero tablero = new Tablero(TAMANIO, TAMANIO);
        for (int columna = 0; columna < TAMANIO; columna++) {
            tablero.colocarPieza(new Posicion(0, columna), fabrica.crear(FILA_TRASERA[columna], Color.NEGRO));
            tablero.colocarPieza(new Posicion(1, columna), fabrica.crear(TipoPieza.PEON, Color.NEGRO));
            tablero.colocarPieza(new Posicion(TAMANIO - 2, columna), fabrica.crear(TipoPieza.PEON, Color.BLANCO));
            tablero.colocarPieza(new Posicion(TAMANIO - 1, columna), fabrica.crear(FILA_TRASERA[columna], Color.BLANCO));
        }
        return tablero;
    }
}
