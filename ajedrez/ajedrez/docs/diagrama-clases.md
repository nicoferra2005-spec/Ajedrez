# Diagrama de clases (UML)

Refleja el código real del repositorio. Se renderiza directamente en GitHub.

```mermaid
classDiagram
direction TB

class ControladorAjedrez {
  -Partida partida
  -VistaAjedrez vista
  +ControladorAjedrez(partida, vista)
  +iniciar() void
  +solicitarMovimiento(origen, destino) void
  -actualizarVista() void
}

class VistaAjedrez {
  <<interface>>
  +mostrarTablero(partida) void
  +mostrarTurno(color) void
  +mostrarMensaje(mensaje) void
  +mostrarJaque(color) void
}

class VistaConsola {
  -PrintStream salida
  +mostrarTablero(partida) void
  +mostrarTurno(color) void
  +mostrarMensaje(mensaje) void
  +mostrarJaque(color) void
}

class AplicacionConsola {
  +ejecutar() void
}

class NotacionAlgebraica {
  +parsear(texto, filas, columnas)$ Posicion
  +formatear(posicion, filas)$ String
}

class Partida {
  -Tablero tablero
  -Jugador blancas
  -Jugador negras
  -Color turnoActual
  -ReglasAjedrez reglas
  +Partida(tablero, blancas, negras, reglas)
  +intentarMover(origen, destino) ResultadoMovimiento
  +obtenerTurno() Color
  +obtenerJugadorActual() Jugador
  +obtenerFilas() int
  +obtenerColumnas() int
  +obtenerPieza(posicion) Pieza
  +obtenerMovimientosLegales(origen) List~Posicion~
  +estaEnJaque(color) boolean
  -alternarTurno() void
}

class ResultadoMovimiento {
  <<value object>>
  -boolean aceptado
  -String mensaje
  -Pieza piezaCapturada
  -boolean siguienteJugadorEnJaque
  +aceptado(piezaCapturada, jaque)$ ResultadoMovimiento
  +rechazado(motivo)$ ResultadoMovimiento
  +fueAceptado() boolean
  +obtenerMensaje() String
  +obtenerCaptura() Pieza
  +hayCaptura() boolean
  +hayJaque() boolean
}

class ReglasAjedrez {
  +esMovimientoLegal(tablero, turno, origen, destino) boolean
  +motivoDeIlegalidad(tablero, turno, origen, destino) Optional~String~
  +movimientosLegales(tablero, turno, origen) List~Posicion~
  +estaEnJaque(tablero, color) boolean
  +estaAtacada(tablero, posicion, atacante) boolean
  +dejaReyEnJaque(tablero, color, origen, destino) boolean
}

class Tablero {
  -int filas
  -int columnas
  -Pieza[][] casillas
  +Tablero(filas, columnas)
  +obtenerFilas() int
  +obtenerColumnas() int
  +estaDentro(posicion) boolean
  +obtenerPieza(posicion) Pieza
  +estaVacia(posicion) boolean
  +colocarPieza(posicion, pieza) void
  +retirarPieza(posicion) Pieza
  +buscarRey(color) Posicion
  +obtenerPosicionesOcupadas(color) List~Posicion~
  +caminoLibre(origen, destino) boolean
  +copiar() Tablero
  +aplicarMovimiento(origen, destino) Pieza
}

class Pieza {
  -Color color
  -TipoPieza tipo
  -EstrategiaMovimiento estrategia
  +Pieza(color, tipo, estrategia)
  +obtenerColor() Color
  +obtenerTipo() TipoPieza
  +destinosPosibles(origen, tablero) List~Posicion~
  +ataca(origen, destino, tablero) boolean
}

class Jugador {
  -String nombre
  -Color color
  +Jugador(nombre, color)
  +obtenerNombre() String
  +obtenerColor() Color
}

class Posicion {
  <<value object>>
  -int fila
  -int columna
  +Posicion(fila, columna)
  +obtenerFila() int
  +obtenerColumna() int
  +desplazar(deltaFila, deltaColumna) Posicion
  +equals(otro) boolean
  +hashCode() int
}

class Color {
  <<enumeration>>
  BLANCO
  NEGRO
  +opuesto() Color
}

class TipoPieza {
  <<enumeration>>
  PEON
  TORRE
  CABALLO
  ALFIL
  REINA
  REY
}

class EstrategiaMovimiento {
  <<interface>>
  +destinosPosibles(pieza, origen, tablero) List~Posicion~
  +ataca(pieza, origen, destino, tablero) boolean
}

class MovimientoPeon
class MovimientoTorre
class MovimientoCaballo
class MovimientoAlfil
class MovimientoReina
class MovimientoRey

class FabricaPiezas {
  +registrar(tipo, estrategia) FabricaPiezas
  +crear(tipo, color) Pieza
}

class FabricaPartidas {
  +partidaEstandar(nombreBlancas, nombreNegras)$ Partida
  +tableroEstandar(fabrica)$ Tablero
}

ControladorAjedrez --> Partida : ejecuta acciones
ControladorAjedrez --> VistaAjedrez : presenta
VistaConsola ..|> VistaAjedrez
VistaConsola ..> Partida : consulta
VistaConsola ..> NotacionAlgebraica
AplicacionConsola --> ControladorAjedrez
AplicacionConsola ..> NotacionAlgebraica

Partida *-- Tablero : contiene
Partida --> ReglasAjedrez : valida con
Partida --> "2" Jugador : participantes
Partida --> Color : turno
Partida ..> ResultadoMovimiento : devuelve
ResultadoMovimiento --> "0..1" Pieza : captura

ReglasAjedrez ..> Tablero : consulta y simula
ReglasAjedrez ..> Pieza : consulta ataques

Tablero o-- "0..*" Pieza : ubica
Tablero ..> Posicion
Pieza --> Color
Pieza --> TipoPieza
Pieza --> EstrategiaMovimiento : delega
Jugador --> Color

EstrategiaMovimiento ..> Tablero : consulta
EstrategiaMovimiento ..> Posicion
MovimientoPeon ..|> EstrategiaMovimiento
MovimientoTorre ..|> EstrategiaMovimiento
MovimientoCaballo ..|> EstrategiaMovimiento
MovimientoAlfil ..|> EstrategiaMovimiento
MovimientoReina ..|> EstrategiaMovimiento
MovimientoRey ..|> EstrategiaMovimiento

FabricaPiezas ..> Pieza : crea
FabricaPiezas ..> EstrategiaMovimiento : registra
FabricaPartidas ..> Partida : arma
```

> `Recorridos` (helper interno package-private del paquete `movimiento`) no se dibuja a propósito.
