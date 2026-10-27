package ajedrez.nucleo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PosicionTest {

    @Test
    void doPosicionesConLasMismasCoordenadasSonIguales() {
        assertEquals(new Posicion(2, 3), new Posicion(2, 3));
        assertEquals(new Posicion(2, 3).hashCode(), new Posicion(2, 3).hashCode());
        assertNotEquals(new Posicion(2, 3), new Posicion(3, 2));
    }

    @Test
    void desplazarDevuelveUnaNuevaPosicionSinModificarLaOriginal() {
        Posicion original = new Posicion(4, 4);
        Posicion movida = original.desplazar(-1, 2);
        assertEquals(new Posicion(3, 6), movida);
        assertEquals(new Posicion(4, 4), original);
    }
}
