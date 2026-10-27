package ajedrez.nucleo;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Guardián de la arquitectura: el núcleo no puede depender de la vista, del controlador
 * ni de ningún framework / librería gráfica. Si alguien agrega un import prohibido, el test falla.
 */
class ArquitecturaNucleoTest {

    private static final List<String> PROHIBIDOS = List.of(
            "import ajedrez.vista", "import ajedrez.controlador",
            "import java.awt", "import javax.swing", "import javafx",
            "import org.springframework", "import java.sql", "import java.net", "import java.io");

    @Test
    void elNucleoNoImportaAdaptadoresNiInfraestructura() throws IOException {
        Path raiz = Path.of("src/main/java/ajedrez/nucleo");
        assertTrue(Files.isDirectory(raiz), "Los tests deben ejecutarse desde la raíz del proyecto");
        try (Stream<Path> archivos = Files.walk(raiz)) {
            List<String> violaciones = archivos
                    .filter(p -> p.toString().endsWith(".java"))
                    .flatMap(p -> lineasProhibidas(p).stream())
                    .collect(Collectors.toList());
            assertTrue(violaciones.isEmpty(), "Imports prohibidos en el núcleo: " + violaciones);
        }
    }

    private static List<String> lineasProhibidas(Path archivo) {
        try {
            return Files.readAllLines(archivo).stream()
                    .map(String::trim)
                    .filter(linea -> PROHIBIDOS.stream().anyMatch(linea::startsWith))
                    .map(linea -> archivo.getFileName() + ": " + linea)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
