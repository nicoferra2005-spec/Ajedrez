package ajedrez.controlador;

import ajedrez.nucleo.Partida;
import ajedrez.nucleo.Posicion;
import ajedrez.nucleo.ResultadoMovimiento;
import ajedrez.vista.VistaAjedrez;
import java.util.Objects;

/**
 * Adaptador entre una vista y el núcleo: traduce pedidos de movimiento en llamadas a
 * {@link Partida} y presenta el resultado a través de {@link VistaAjedrez}.
 * No contiene reglas del juego.
 */
public class ControladorAjedrez {

    private final Partida partida;
    private final VistaAjedrez vista;

    public ControladorAjedrez(Partida partida, VistaAjedrez vista) {
        this.partida = Objects.requireNonNull(partida, "partida");
        this.vista = Objects.requireNonNull(vista, "vista");
    }

    public void iniciar() {
        vista.mostrarMensaje("¡Comienza la partida!");
        actualizarVista();
    }

    public void solicitarMovimiento(Posicion origen, Posicion destino) {
        ResultadoMovimiento resultado = partida.intentarMover(origen, destino);
        if (!resultado.fueAceptado()) {
            vista.mostrarMensaje("Movimiento inválido: " + resultado.obtenerMensaje());
            return;
        }
        if (resultado.hayCaptura()) {
            vista.mostrarMensaje("Captura: " + resultado.obtenerCaptura());
        }
        actualizarVista();
        if (resultado.hayJaque()) {
            vista.mostrarJaque(partida.obtenerTurno());
        }
    }

    private void actualizarVista() {
        vista.mostrarTablero(partida);
        vista.mostrarTurno(partida.obtenerTurno());
    }
}
