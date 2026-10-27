package ajedrez.nucleo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Orquesta una partida: tablero, jugadores, turno y reglas. Es el único punto de entrada
 * al núcleo para quien quiera jugar (consola, GUI, tests...). Empieza jugando las blancas
 * y el turno solo se alterna cuando el movimiento fue aceptado.
 */
public class Partida {

    private final Tablero tablero;
    private final Jugador blancas;
    private final Jugador negras;
    private final ReglasAjedrez reglas;
    private Color turnoActual;

    public Partida(Tablero tablero, Jugador blancas, Jugador negras, ReglasAjedrez reglas) {
        this.tablero = Objects.requireNonNull(tablero, "tablero");
        this.blancas = Objects.requireNonNull(blancas, "blancas");
        this.negras = Objects.requireNonNull(negras, "negras");
        this.reglas = Objects.requireNonNull(reglas, "reglas");
        if (blancas.obtenerColor() != Color.BLANCO || negras.obtenerColor() != Color.NEGRO) {
            throw new IllegalArgumentException("Cada jugador debe tener el color que le corresponde.");
        }
        this.turnoActual = Color.BLANCO;
    }

    public ResultadoMovimiento intentarMover(Posicion origen, Posicion destino) {
        Optional<String> motivo = reglas.motivoDeIlegalidad(tablero, turnoActual, origen, destino);
        if (motivo.isPresent()) {
            return ResultadoMovimiento.rechazado(motivo.get());
        }
        Pieza capturada = tablero.aplicarMovimiento(origen, destino);
        alternarTurno();
        return ResultadoMovimiento.aceptado(capturada, reglas.estaEnJaque(tablero, turnoActual));
    }

    public Color obtenerTurno() {
        return turnoActual;
    }

    public Jugador obtenerJugadorActual() {
        return turnoActual == Color.BLANCO ? blancas : negras;
    }

    public int obtenerFilas() {
        return tablero.obtenerFilas();
    }

    public int obtenerColumnas() {
        return tablero.obtenerColumnas();
    }

    /** @return la pieza en la posición, o {@code null} si está vacía. */
    public Pieza obtenerPieza(Posicion posicion) {
        return tablero.obtenerPieza(posicion);
    }

    public List<Posicion> obtenerMovimientosLegales(Posicion origen) {
        return reglas.movimientosLegales(tablero, turnoActual, origen);
    }

    public boolean estaEnJaque(Color color) {
        return reglas.estaEnJaque(tablero, color);
    }

    private void alternarTurno() {
        turnoActual = turnoActual.opuesto();
    }
}
