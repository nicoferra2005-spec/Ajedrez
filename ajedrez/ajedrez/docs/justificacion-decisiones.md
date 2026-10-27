# Justificación de decisiones de diseño

> **BORRADOR para que el grupo lo revise y lo haga propio.** Está escrito sobre las decisiones que
> efectivamente tomó el código, en el formato pedido (qué / por qué / cuándo se rompería). En la defensa
> individual cada integrante tiene que poder explicar y discutir cada punto, así que conviene ajustar
> la redacción a lo que el grupo realmente piensa.

## 1. El núcleo no depende de la interfaz ni de infraestructura

- **Qué:** las reglas viven en el paquete `nucleo`. La salida se hace por la interfaz `VistaAjedrez` y la
  entrada por `ControladorAjedrez`; el único que conecta todo es `Main`.
- **Por qué:** permite testear las reglas sin consola ni GUI, y cambiar la interfaz sin tocar el juego
  (inversión de dependencias, puertos y adaptadores). `ArquitecturaNucleoTest` lo vigila.
- **Cuándo se rompería:** si el núcleo empezara a imprimir, leer de teclado o persistir datos; o si una
  segunda interfaz necesitara información que el núcleo no expone y se "filtrara" lógica de juego a la vista.

## 2. Strategy para el movimiento de las piezas

- **Qué:** `Pieza` delega en una `EstrategiaMovimiento`. Hay una estrategia por tipo de pieza.
- **Por qué:** agregar una pieza no modifica clases existentes (Open/Closed); cada movimiento es testeable
  por separado (Single Responsibility); las estrategias no tienen estado y se comparten.
- **Cuándo se rompería:** cuando el movimiento depende del *historial* (enroque, captura al paso: hay que saber
  si la pieza ya se movió o cuál fue la última jugada). Una estrategia sin estado no alcanza; habría que pasar
  historial a la estrategia o mover esa lógica a `Partida`/`ReglasAjedrez`.

## 3. Composición sobre herencia

- **Qué:** no hay subclases `Peon`, `Torre`... Una sola clase `Pieza` compone una estrategia.
- **Por qué:** evita una jerarquía rígida y duplicada; permite cambiar el comportamiento sin cambiar la clase
  (el test de la "Amazona" lo demuestra).
- **Cuándo se rompería:** si las piezas tuvieran datos y comportamiento muy distintos entre sí (por ejemplo
  un peón con estado de "ya se movió" y una torre con estado de "puede enrocar"). Ahí `Pieza` tendría que
  crecer con campos que no aplican a todas.

## 4. Reglas separadas del tablero y de la partida

- **Qué:** `Tablero` = geometría y ocupación; `ReglasAjedrez` = legalidad y jaque; `Partida` = turnos.
- **Por qué:** una responsabilidad por clase (SRP); se pueden probar reglas con tableros armados a mano.
- **Cuándo se rompería:** si se quisieran variantes con reglas distintas, `ReglasAjedrez` debería pasar a
  una interfaz (hoy `Partida` depende de la clase concreta).

## 5. Detectar si un movimiento deja al rey en jaque simulándolo en una copia

- **Qué:** `dejaReyEnJaque` aplica el movimiento sobre `Tablero.copiar()` y consulta `estaEnJaque`.
- **Por qué:** es simple, correcto y no puede corromper el tablero real; reutiliza `ataca` de las piezas.
- **Cuándo se rompería:** en rendimiento, si se agrega una IA que evalúe miles de jugadas por segundo (copiar
  el tablero cada vez es costoso). Ahí convendría un mecanismo de mover/deshacer (Command) sobre el mismo tablero.

## 6. Value objects inmutables (`Posicion`, `ResultadoMovimiento`)

- **Qué:** objetos sin setters, con `equals/hashCode` por valor.
- **Por qué:** se pueden compartir sin riesgo, usar en `Set`/`Map` y comparar en tests.
- **Cuándo se rompería:** si hiciera falta mutar una posición en bucles muy intensivos, aunque no es el caso.

## 7. Tablero parametrizable

- **Qué:** `new Tablero(filas, columnas)`; el peón deduce su fila inicial del tamaño.
- **Por qué:** una extensión de "otro tamaño de tablero" es un cambio de parámetro, no de código.
- **Cuándo se rompería:** si una pieza codificara coordenadas fijas (por ejemplo "fila 6") o si la notación
  necesitara más de 26 columnas (hoy usa letras `a`–`z`).

## 8. `FabricaPiezas` como único punto de asociación tipo → estrategia

- **Qué:** un registro `EnumMap<TipoPieza, EstrategiaMovimiento>`.
- **Por qué:** cambiar o agregar un movimiento se hace en un solo lugar.
- **Cuándo se rompería:** `TipoPieza` es un enum cerrado: un tipo totalmente nuevo exige modificarlo
  (violación parcial de Open/Closed). Se podría reemplazar por un identificador abierto si hiciera falta.
