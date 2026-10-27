package ajedrez.nucleo;

import ajedrez.nucleo.movimiento.EstrategiaMovimiento;
import ajedrez.nucleo.movimiento.MovimientoAlfil;
import ajedrez.nucleo.movimiento.MovimientoCaballo;
import ajedrez.nucleo.movimiento.MovimientoPeon;
import ajedrez.nucleo.movimiento.MovimientoReina;
import ajedrez.nucleo.movimiento.MovimientoRey;
import ajedrez.nucleo.movimiento.MovimientoTorre;
import java.util.EnumMap;
import java.util.Map;

/**
 * Único lugar que asocia cada {@link TipoPieza} con su {@link EstrategiaMovimiento}.
 * Como las estrategias no tienen estado, se comparte una instancia por tipo.
 * Se puede registrar o reemplazar un movimiento sin tocar el resto del sistema.
 */
public class FabricaPiezas {

    private final Map<TipoPieza, EstrategiaMovimiento> estrategias = new EnumMap<>(TipoPieza.class);

    public FabricaPiezas() {
        registrar(TipoPieza.PEON, new MovimientoPeon());
        registrar(TipoPieza.TORRE, new MovimientoTorre());
        registrar(TipoPieza.CABALLO, new MovimientoCaballo());
        registrar(TipoPieza.ALFIL, new MovimientoAlfil());
        registrar(TipoPieza.REINA, new MovimientoReina());
        registrar(TipoPieza.REY, new MovimientoRey());
    }

    /** Asocia (o reemplaza) la estrategia de un tipo. Devuelve this para encadenar. */
    public FabricaPiezas registrar(TipoPieza tipo, EstrategiaMovimiento estrategia) {
        estrategias.put(tipo, estrategia);
        return this;
    }

    public Pieza crear(TipoPieza tipo, Color color) {
        EstrategiaMovimiento estrategia = estrategias.get(tipo);
        if (estrategia == null) {
            throw new IllegalArgumentException("No hay movimiento registrado para " + tipo);
        }
        return new Pieza(color, tipo, estrategia);
    }
}
