package ajedrez.nucleo;

/**
 * Value object inmutable con el resultado de intentar un movimiento.
 * Se crea con {@link #aceptado} o {@link #rechazado}.
 */
public final class ResultadoMovimiento {

    private final boolean aceptado;
    private final String mensaje;
    private final Pieza piezaCapturada;
    private final boolean siguienteJugadorEnJaque;

    private ResultadoMovimiento(boolean aceptado, String mensaje, Pieza piezaCapturada,
                                boolean siguienteJugadorEnJaque) {
        this.aceptado = aceptado;
        this.mensaje = mensaje;
        this.piezaCapturada = piezaCapturada;
        this.siguienteJugadorEnJaque = siguienteJugadorEnJaque;
    }

    /** @param piezaCapturada pieza capturada, o {@code null} si no hubo captura. */
    public static ResultadoMovimiento aceptado(Pieza piezaCapturada, boolean siguienteJugadorEnJaque) {
        return new ResultadoMovimiento(true, "Movimiento aceptado.", piezaCapturada, siguienteJugadorEnJaque);
    }

    public static ResultadoMovimiento rechazado(String motivo) {
        return new ResultadoMovimiento(false, motivo, null, false);
    }

    public boolean fueAceptado() {
        return aceptado;
    }

    public String obtenerMensaje() {
        return mensaje;
    }

    /** @return la pieza capturada, o {@code null} si no hubo captura (ver {@link #hayCaptura()}). */
    public Pieza obtenerCaptura() {
        return piezaCapturada;
    }

    public boolean hayCaptura() {
        return piezaCapturada != null;
    }

    /** @return true si, tras el movimiento, el jugador que debe mover queda en jaque. */
    public boolean hayJaque() {
        return siguienteJugadorEnJaque;
    }
}
