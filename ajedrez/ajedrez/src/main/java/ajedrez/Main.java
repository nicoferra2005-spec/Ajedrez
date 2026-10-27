package ajedrez;

import ajedrez.controlador.ControladorAjedrez;
import ajedrez.nucleo.FabricaPartidas;
import ajedrez.nucleo.Partida;
import ajedrez.vista.consola.AplicacionConsola;
import ajedrez.vista.consola.VistaConsola;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/** Composition root: es el único lugar donde se conectan núcleo, controlador y vista. */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) throws IOException {
        PrintStream salida = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        Partida partida = FabricaPartidas.partidaEstandar("Blancas", "Negras");
        ControladorAjedrez controlador = new ControladorAjedrez(partida, new VistaConsola(salida));
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        new AplicacionConsola(controlador, partida.obtenerFilas(), partida.obtenerColumnas(), entrada, salida).ejecutar();
    }
}
