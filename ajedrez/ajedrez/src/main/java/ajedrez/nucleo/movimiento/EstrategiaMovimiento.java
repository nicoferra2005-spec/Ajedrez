package ajedrez.nucleo.movimiento;

import ajedrez.nucleo.Pieza;
import ajedrez.nucleo.Posicion;
import ajedrez.nucleo.Tablero;
import java.util.List;

/**
 * Patrón Strategy: define cómo se mueve y a qué casillas amenaza una pieza.
 * Para agregar un movimiento nuevo basta con implementar esta interfaz;
 * ninguna clase existente se modifica (Open/Closed).
 *
 * Las implementaciones deben ser stateless: reciben todo lo que necesitan por parámetro.
 */
public interface EstrategiaMovimiento {

    /**
     * Casillas a las que {@code pieza} puede moverse desde {@code origen} según su patrón y la
     * ocupación del tablero (incluye capturas de piezas rivales). No valida el jaque propio.
     */
    List<Posicion> destinosPosibles(Pieza pieza, Posicion origen, Tablero tablero);

    /**
     * @return true si {@code pieza}, ubicada en {@code origen}, amenaza {@code destino}.
     *         No considera el color de lo que haya en destino ni el jaque propio.
     */
    boolean ataca(Pieza pieza, Posicion origen, Posicion destino, Tablero tablero);
}
