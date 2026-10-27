package ajedrez.nucleo;

import java.util.Objects;

/** Participante de una partida. */
public final class Jugador {

    private final String nombre;
    private final Color color;

    public Jugador(String nombre, Color color) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El jugador necesita un nombre.");
        }
        this.nombre = nombre;
        this.color = Objects.requireNonNull(color, "color");
    }

    public String obtenerNombre() {
        return nombre;
    }

    public Color obtenerColor() {
        return color;
    }
}
