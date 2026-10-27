package ajedrez.vista;

import ajedrez.nucleo.Color;
import ajedrez.nucleo.Partida;

/**
 * Puerto de salida: lo que el controlador necesita mostrar. El núcleo no conoce esta interfaz,
 * así que se puede reemplazar la consola por una GUI, una web o un mock de test.
 */
public interface VistaAjedrez {

    void mostrarTablero(Partida partida);

    void mostrarTurno(Color color);

    void mostrarMensaje(String mensaje);

    void mostrarJaque(Color color);
}
