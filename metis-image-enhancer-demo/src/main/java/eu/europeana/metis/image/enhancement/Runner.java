package eu.europeana.metis.image.enhancement;

import eu.europeana.metis.image.enhancement.domain.worker.ImageEnhancerWorker;
import eu.europeana.metis.image.enhancement.client.ImageEnhancerClient;
import eu.europeana.metis.image.enhancement.config.ImageEnhancerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * The type Runner.
 */
public class Runner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ImageEnhancerClientConfig imageEnhancerClientConfig;

    /**
     * Instantiates a new Runner.
     *
     * @param imageEnhancerClientConfig the image enhancer client config
     */
    public Runner(ImageEnhancerClientConfig imageEnhancerClientConfig) {
        this.imageEnhancerClientConfig = imageEnhancerClientConfig;
    }

    @Override
    public void run(String... args) throws IOException {
        ImageEnhancerWorker imageEnhancerWorker = new ImageEnhancerWorker(new ImageEnhancerClient(imageEnhancerClientConfig));
        byte[] sourceFile;
        try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("img/europeana_demo.png")){
            sourceFile = Objects.requireNonNull(inputStream).readAllBytes();
        }

        try {
            Files.write(Paths.get("src", "main", "resources", "img", "europeana_demo_enhanced.png")
                    .toAbsolutePath(), imageEnhancerWorker.enhance(sourceFile), StandardOpenOption.CREATE);
        } catch (IOException e) {
            LOGGER.error("saving image file", e);
        }
    }
}
