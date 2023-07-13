package eu.europeana.metis.isr.infrastructure;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The type Python api enhancer integration test.
 */
@Testcontainers
class PythonApiEnhancerIT {

    @Container
    public static final GenericContainer ISR_CONTAINER = new GenericContainer(
            new ImageFromDockerfile()
                    .withDockerfile(Paths.get("src", "main", "resources", "isr", "app", "Dockerfile").toAbsolutePath()))
            .withExposedPorts(5050)
            .waitingFor(Wait.forHttp("/"));

    static {
        ISR_CONTAINER.start();
    }

    @Test
    void givenImage_whenPythonApiServerInvoked_thenSuccess() {

        // given
        final Path resourceDirectory = Paths.get("src", "test", "resources");
        final String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        final String testInputFile = absolutePath + "/img/thumbnail.jpg";
        PythonApiEnhancer pythonApiEnhancer = new PythonApiEnhancer("http://localhost:" + ISR_CONTAINER.getMappedPort(5050), 300, 300);

        // when
        final BufferedImage testOutputFile = pythonApiEnhancer.enhance(testInputFile);

        // then
        assertNotNull(testOutputFile);
    }
}
