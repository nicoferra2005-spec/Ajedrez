package ajedrez.vista.consola;

import ajedrez.controlador.ControladorAjedrez;
import ajedrez.nucleo.Posicion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

/** Bucle de entrada por consola: lee comandos como "e2 e4" y se los pasa al controlador. */
public class AplicacionConsola {

    private final ControladorAjedrez controlador;
    private final int filas;
    private final int columnas;
    private final BufferedReader entrada;
    private final PrintStream salida;

    public AplicacionConsola(ControladorAjedrez controlador, int filas, int columnas,
                             BufferedReader entrada, PrintStream salida) {
        this.controlador = controlador;
        this.filas = filas;
        this.columnas = columnas;
        this.entrada = entrada;
        this.salida = salida;
    }

    public void ejecutar() throws IOException {
        controlador.iniciar();
        mostrarAyuda();
        while (true) {
            salida.print("> ");
            salida.flush();
            String linea = entrada.readLine();
            if (linea == null || linea.trim().equalsIgnoreCase("salir")) {
                salida.println("Fin de la partida.");
                return;
            }
            if (linea.trim().equalsIgnoreCase("ayuda")) {
                mostrarAyuda();
                continue;
            }
            procesar(linea);
        }
    }

    private void procesar(String linea) {
        String[] partes = linea.trim().split("\\s+");
        if (partes.length != 2) {
            salida.println("Formato inválido. Escribí origen y destino, por ejemplo: e2 e4");
            return;
        }
        try {
            Posicion origen = NotacionAlgebraica.parsear(partes[0], filas, columnas);
            Posicion destino = NotacionAlgebraica.parsear(partes[1], filas, columnas);
            controlador.solicitarMovimiento(origen, destino);
        } catch (IllegalArgumentException e) {
            salida.println(e.getMessage());
        }
    }

    private void mostrarAyuda() {
        salida.println("Comandos: 'e2 e4' (mover), 'ayuda', 'salir'.");
        salida.println("Piezas: P=Peón T=Torre C=Caballo A=Alfil D=Dama R=Rey (MAYÚSCULA=blancas, minúscula=negras)");
    }
}
