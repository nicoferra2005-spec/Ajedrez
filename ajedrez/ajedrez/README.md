# Ajedrez — TPO Ingeniería de Software

Implementación del juego de ajedrez en Java 17, diseñada para ser **modular, flexible y escalable**.
El núcleo (reglas del juego) no depende de ningún framework, motor gráfico ni infraestructura, y se prueba
con tests unitarios sin levantar nada.

## Cómo correrlo

Requisitos: JDK 17+ y Maven.

```bash
mvn test                              # corre los tests del núcleo
mvn package                           # compila, testea y genera target/ajedrez-1.0.0.jar
java -jar target/ajedrez-1.0.0.jar    # juega por consola
```

Comandos en el juego: `e2 e4` (mover), `ayuda`, `salir`.
Las blancas se muestran en MAYÚSCULA y las negras en minúscula: `P` peón, `T` torre, `C` caballo,
`A` alfil, `D` dama/reina, `R` rey.

## Alcance cubierto (obligatorio)

| Requisito | Dónde está |
|---|---|
| Tablero 8x8 | `Tablero` (parametrizable) + `FabricaPartidas` |
| 6 piezas con movimiento propio | `nucleo/movimiento/Movimiento*.java` |
| Alternancia de turnos | `Partida` |
| Captura de piezas | `Tablero.aplicarMovimiento`, `ResultadoMovimiento.obtenerCaptura` |
| Validación de movimientos inválidos | `ReglasAjedrez.motivoDeIlegalidad` |
| Detección de jaque | `ReglasAjedrez.estaEnJaque` / `estaAtacada` |

Alcance opcional **no** implementado (por decisión): jaque mate, enroque, captura al paso, promoción,
tablas, IA, deshacer/rehacer y GUI. La arquitectura está pensada para sumarlos sin romper lo existente
(ver *Cómo extender*).

## Arquitectura

```
ajedrez
├── nucleo                      ← dominio puro (sin dependencias externas)
│   ├── Posicion, Color, TipoPieza, Jugador          (value objects / enums)
│   ├── Pieza, Tablero, Partida, ReglasAjedrez
│   ├── ResultadoMovimiento                          (value object)
│   ├── FabricaPiezas, FabricaPartidas               (armado de objetos)
│   └── movimiento
│       ├── EstrategiaMovimiento                     (Strategy)
│       └── MovimientoPeon/Torre/Caballo/Alfil/Reina/Rey
├── vista                       ← adaptador de salida
│   ├── VistaAjedrez            (interfaz / puerto)
│   └── consola: VistaConsola, AplicacionConsola, NotacionAlgebraica
├── controlador                 ← adaptador de entrada
│   └── ControladorAjedrez
└── Main                        ← composition root (único lugar que conecta todo)
```

Reglas de dependencia: `vista` y `controlador` conocen al `nucleo`; **el núcleo no conoce a nadie**.
El test `ArquitecturaNucleoTest` lo verifica automáticamente (falla si el núcleo importa vista,
controlador, Swing/AWT, Spring, red o I/O).

Ideas de diseño principales:

- **Composición sobre herencia:** `Pieza` es una única clase que *tiene* una `EstrategiaMovimiento`;
  no existen subclases `Peon`, `Torre`, etc.
- **Strategy:** cada movimiento es una clase independiente y sin estado (se comparte una instancia por tipo).
- **Separación de responsabilidades:** `Tablero` conoce geometría y ocupación; `ReglasAjedrez` decide
  la legalidad y el jaque; `Partida` orquesta turnos; el controlador y la vista solo adaptan.
- **Simulación en copia:** para saber si un movimiento deja al propio rey en jaque, `ReglasAjedrez`
  lo aplica sobre `Tablero.copiar()`. El tablero real nunca se toca hasta que el movimiento es válido.
- **Inmutabilidad:** `Posicion`, `Pieza` y `ResultadoMovimiento` son inmutables.

## Cómo extender

- **Pieza nueva:** crear una clase que implemente `EstrategiaMovimiento` y registrarla con
  `FabricaPiezas.registrar(...)`. Ver `ExtensibilidadTest` (una "Amazona" = reina + caballo, sin tocar código existente).
  Nota: `TipoPieza` es un enum, así que un tipo totalmente nuevo requiere sumar un valor al enum.
- **Otro tamaño de tablero:** `new Tablero(filas, columnas)`. El peón deduce su fila inicial del tamaño.
- **Otra interfaz (GUI, web):** implementar `VistaAjedrez` y conectarla en un nuevo `Main`; el núcleo no cambia.

## Diagrama de clases

Ver [`docs/diagrama-clases.md`](docs/diagrama-clases.md) (Mermaid, se renderiza en GitHub).
Documento de justificación: [`docs/justificacion-decisiones.md`](docs/justificacion-decisiones.md).

## Cambios respecto del diagrama original

El código sigue el diagrama de clases del grupo. Para que el UML siga siendo fiel al código, estas son
las únicas diferencias (ya reflejadas en `docs/diagrama-clases.md`):

- `Tablero`: se agregaron `obtenerFilas()` y `obtenerColumnas()`.
- `Partida`: se agregaron `obtenerFilas()`, `obtenerColumnas()` y `obtenerJugadorActual()` (la vista necesita dibujar el tablero).
- `ReglasAjedrez`: se agregó `motivoDeIlegalidad(...)`; `esMovimientoLegal` delega en él. Así el mensaje de error y la validación salen de una sola fuente.
- `ResultadoMovimiento`: constructor privado con fábricas `aceptado(...)` / `rechazado(...)` y el método `hayCaptura()`.
- `Color`: se agregó `opuesto()`.
- Clases nuevas: `FabricaPiezas`, `FabricaPartidas` (armado inicial), `NotacionAlgebraica`, `AplicacionConsola`, `Main`.
- `Recorridos` es un helper interno (package-private) del paquete `movimiento`; no se dibuja en el UML.
