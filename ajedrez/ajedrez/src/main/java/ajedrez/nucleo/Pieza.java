package ajedrez.nucleo;

import ajedrez.nucleo.movimiento.EstrategiaMovimiento;
import java.util.List;
import java.util.Objects;

/**
 * Pieza del tablero. No sabe moverse por sí sola: delega en una
 * {@link EstrategiaMovimiento} (composición sobre herencia). Es inmutable, por lo que
 * se puede compartir entre un tablero y sus copias.
 */
public final class Pieza {

    private final Color color;
    private final TipoPieza tipo;
    private final EstrategiaMovimiento estrategia;

    public Pieza(Color color, TipoPieza tipo, EstrategiaMovimiento estrategia) {
        this.color = Objects.requireNonNull(color, "color");
        this.tipo = Objects.requireNonNull(tipo, "tipo");
        this.estrategia = Objects.requireNonNull(estrategia, "estrategia");
    }

    public Color obtenerColor() {
        return color;
    }

    public TipoPieza obtenerTipo() {
        return tipo;
    }

    /** Casillas a las que puede ir desde {@code origen}, sin considerar el jaque propio. */
    public List<Posicion> destinosPosibles(Posicion origen, Tablero tablero) {
        return estrategia.destinosPosibles(this, origen, tablero);
    }

    /** @return true si, parada en {@code origen}, amenaza la casilla {@code destino}. */
    public boolean ataca(Posicion origen, Posicion destino, Tablero tablero) {
        return estrategia.ataca(this, origen, destino, tablero);
    }

    @Override
    public String toString() {
        return tipo + " " + color;
    }
}
