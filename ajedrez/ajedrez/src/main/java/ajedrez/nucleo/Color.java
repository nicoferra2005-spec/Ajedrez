package ajedrez.nucleo;

/** Color de una pieza o de un jugador. */
public enum Color {
    BLANCO,
    NEGRO;

    /** @return el color contrario. */
    public Color opuesto() {
        return this == BLANCO ? NEGRO : BLANCO;
    }
}
