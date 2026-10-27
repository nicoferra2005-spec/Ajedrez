package ajedrez.nucleo;

import static ajedrez.nucleo.Escenarios.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ReglasAjedrezTest {

    private final ReglasAjedrez reglas = new ReglasAjedrez();

    @Test
    void torreRivalEnLaMismaColumnaDaJaque() {
        Tablero t = vacio();
        poner(t, "e1", TipoPieza.REY, Color.BLANCO);
        poner(t, "e8", TipoPieza.TORRE, Color.NEGRO);
        assertTrue(reglas.estaEnJaque(t, Color.BLANCO));
        assertFalse(reglas.estaEnJaque(t, Color.NEGRO));
    }

    @Test
    void unaPiezaInterpuestaEvitaElJaque() {
        Tablero t = vacio();
        poner(t, "e1", TipoPieza.REY, Color.BLANCO);
        poner(t, "e4", TipoPieza.PEON, Color.BLANCO);
        poner(t, "e8", TipoPieza.TORRE, Color.NEGRO);
        assertFalse(reglas.estaEnJaque(t, Color.BLANCO));
    }

    @Test
    void peonCaballoYAlfilTambienDanJaque() {
        Tablero peon = vacio();
        poner(peon, "e4", TipoPieza.REY, Color.BLANCO);
        poner(peon, "d5", TipoPieza.PEON, Color.NEGRO);
        assertTrue(reglas.estaEnJaque(peon, Color.BLANCO));

        Tablero caballo = vacio();
        poner(caballo, "e4", TipoPieza.REY, Color.BLANCO);
        poner(caballo, "f6", TipoPieza.CABALLO, Color.NEGRO);
        assertTrue(reglas.estaEnJaque(caballo, Color.BLANCO));

        Tablero alfil = vacio();
        poner(alfil, "e4", TipoPieza.REY, Color.BLANCO);
        poner(alfil, "h7", TipoPieza.ALFIL, Color.NEGRO);
        assertTrue(reglas.estaEnJaque(alfil, Color.BLANCO));
    }

    @Test
    void sinReyNoHayJaque() {
        Tablero t = vacio();
        poner(t, "a8", TipoPieza.TORRE, Color.NEGRO);
        assertFalse(reglas.estaEnJaque(t, Color.BLANCO));
    }

    @Test
    void elReyNoPuedeMoverseAUnaCasillaAtacada() {
        Tablero t = vacio();
        poner(t, "e1", TipoPieza.REY, Color.BLANCO);
        poner(t, "d8", TipoPieza.TORRE, Color.NEGRO);
        assertFalse(reglas.esMovimientoLegal(t, Color.BLANCO, pos("e1"), pos("d1")));
        assertTrue(reglas.esMovimientoLegal(t, Color.BLANCO, pos("e1"), pos("e2")));
    }

    @Test
    void unaPiezaClavadaNoPuedeAbandonarLaLineaDelRey() {
        Tablero t = vacio();
        poner(t, "e1", TipoPieza.REY, Color.BLANCO);
        poner(t, "e2", TipoPieza.TORRE, Color.BLANCO);
        poner(t, "e8", TipoPieza.TORRE, Color.NEGRO);
        assertFalse(reglas.esMovimientoLegal(t, Color.BLANCO, pos("e2"), pos("d2")));
        assertTrue(reglas.esMovimientoLegal(t, Color.BLANCO, pos("e2"), pos("e5")));
    }

    @Test
    void estandoEnJaqueSoloSonLegalesLasRespuestasQueLoResuelven() {
        Tablero t = vacio();
        poner(t, "e1", TipoPieza.REY, Color.BLANCO);
        poner(t, "a1", TipoPieza.TORRE, Color.BLANCO);
        poner(t, "e8", TipoPieza.TORRE, Color.NEGRO);
        // mover la torre sin tapar la columna no resuelve el jaque
        assertFalse(reglas.esMovimientoLegal(t, Color.BLANCO, pos("a1"), pos("a5")));
        // el rey solo puede salir de la columna e
        Set<Posicion> legalesRey = new HashSet<>(reglas.movimientosLegales(t, Color.BLANCO, pos("e1")));
        assertEquals(posiciones("d1", "d2", "f1", "f2"), legalesRey);
    }

    @Test
    void sePuedeBloquearElJaqueInterponiendoUnaPieza() {
        Tablero t = vacio();
        poner(t, "e1", TipoPieza.REY, Color.BLANCO);
        poner(t, "a4", TipoPieza.TORRE, Color.BLANCO);
        poner(t, "e8", TipoPieza.TORRE, Color.NEGRO);
        assertTrue(reglas.esMovimientoLegal(t, Color.BLANCO, pos("a4"), pos("e4")));
        assertFalse(reglas.esMovimientoLegal(t, Color.BLANCO, pos("a4"), pos("b4")));
    }

    @Test
    void noSePuedeCapturarAlRey() {
        Tablero t = vacio();
        poner(t, "a1", TipoPieza.TORRE, Color.BLANCO);
        poner(t, "a8", TipoPieza.REY, Color.NEGRO);
        assertFalse(reglas.esMovimientoLegal(t, Color.BLANCO, pos("a1"), pos("a8")));
    }

    @Test
    void nadaSeMueveSiNoEsElTurnoDeEseColor() {
        Tablero t = vacio();
        poner(t, "a1", TipoPieza.TORRE, Color.BLANCO);
        assertFalse(reglas.esMovimientoLegal(t, Color.NEGRO, pos("a1"), pos("a2")));
        assertTrue(reglas.motivoDeIlegalidad(t, Color.NEGRO, pos("a1"), pos("a2")).get().contains("NEGRO"));
    }

    @Test
    void casillaVaciaOFueraDelTableroSonIlegales() {
        Tablero t = vacio();
        poner(t, "a1", TipoPieza.TORRE, Color.BLANCO);
        assertFalse(reglas.esMovimientoLegal(t, Color.BLANCO, pos("b1"), pos("b2")));
        assertFalse(reglas.esMovimientoLegal(t, Color.BLANCO, pos("a1"), new Posicion(-1, 0)));
    }

    @Test
    void simularNoModificaElTableroOriginal() {
        Tablero t = vacio();
        poner(t, "e1", TipoPieza.REY, Color.BLANCO);
        poner(t, "e8", TipoPieza.TORRE, Color.NEGRO);
        reglas.dejaReyEnJaque(t, Color.BLANCO, pos("e1"), pos("d1"));
        assertNotNull(t.obtenerPieza(pos("e1")));
        assertTrue(t.estaVacia(pos("d1")));
    }

    @Test
    void movimientosLegalesDeUnaPiezaAjenaOVaciaEstanVacios() {
        Tablero t = vacio();
        poner(t, "a1", TipoPieza.TORRE, Color.NEGRO);
        assertTrue(reglas.movimientosLegales(t, Color.BLANCO, pos("a1")).isEmpty());
        assertTrue(reglas.movimientosLegales(t, Color.BLANCO, pos("h5")).isEmpty());
    }
}
