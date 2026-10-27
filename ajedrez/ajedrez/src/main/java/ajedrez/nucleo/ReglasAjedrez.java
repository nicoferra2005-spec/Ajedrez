package ajedrez.nucleo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Reglas de validación de movimientos y detección de jaque. No guarda estado: opera sobre el
 * tablero que recibe. Para saber si un movimiento deja al propio rey en jaque lo simula sobre
 * una copia, sin tocar el tablero real.
 */
public class ReglasAjedrez {

    public boolean esMovimientoLegal(Tablero tablero, Color turno, Posicion origen, Posicion destino) {
        return motivoDeIlegalidad(tablero, turno, origen, destino).isEmpty();
    }

    /** @return el motivo por el cual el movimiento es ilegal, o vacío si es legal. */
    public Optional<String> motivoDeIlegalidad(Tablero tablero, Color turno, Posicion origen, Posicion destino) {
        if (!tablero.estaDentro(origen) || !tablero.estaDentro(destino)) {
            return Optional.of("La posición está fuera del tablero.");
        }
        Pieza pieza = tablero.obtenerPieza(origen);
        if (pieza == null) {
            return Optional.of("No hay ninguna pieza en la casilla de origen.");
        }
        if (pieza.obtenerColor() != turno) {
            return Optional.of("Esa pieza no es tuya: es el turno de las " + turno + ".");
        }
        if (!pieza.destinosPosibles(origen, tablero).contains(destino)) {
            return Optional.of("La pieza no puede moverse de esa manera.");
        }
        Pieza ocupante = tablero.obtenerPieza(destino);
        if (ocupante != null && ocupante.obtenerTipo() == TipoPieza.REY) {
            return Optional.of("No se puede capturar al rey.");
        }
        if (dejaReyEnJaque(tablero, turno, origen, destino)) {
            return Optional.of("Ese movimiento deja a tu rey en jaque.");
        }
        return Optional.empty();
    }

    /** @return los destinos legales de la pieza en {@code origen}; vacío si no es del turno o no hay pieza. */
    public List<Posicion> movimientosLegales(Tablero tablero, Color turno, Posicion origen) {
        List<Posicion> legales = new ArrayList<>();
        if (!tablero.estaDentro(origen)) {
            return legales;
        }
        Pieza pieza = tablero.obtenerPieza(origen);
        if (pieza == null || pieza.obtenerColor() != turno) {
            return legales;
        }
        for (Posicion destino : pieza.destinosPosibles(origen, tablero)) {
            if (esMovimientoLegal(tablero, turno, origen, destino)) {
                legales.add(destino);
            }
        }
        return legales;
    }

    /** @return true si el rey de {@code color} está amenazado. Sin rey en el tablero, devuelve false. */
    public boolean estaEnJaque(Tablero tablero, Color color) {
        Posicion rey = tablero.buscarRey(color);
        return rey != null && estaAtacada(tablero, rey, color.opuesto());
    }

    /** @return true si alguna pieza de {@code atacante} amenaza la casilla indicada. */
    public boolean estaAtacada(Tablero tablero, Posicion posicion, Color atacante) {
        for (Posicion origen : tablero.obtenerPosicionesOcupadas(atacante)) {
            if (tablero.obtenerPieza(origen).ataca(origen, posicion, tablero)) {
                return true;
            }
        }
        return false;
    }

    /** Simula el movimiento en una copia y consulta si el rey de {@code color} queda en jaque. */
    public boolean dejaReyEnJaque(Tablero tablero, Color color, Posicion origen, Posicion destino) {
        Tablero simulacion = tablero.copiar();
        simulacion.aplicarMovimiento(origen, destino);
        return estaEnJaque(simulacion, color);
    }
}
