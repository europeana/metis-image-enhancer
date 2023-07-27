package eu.europeana.metis.isr;

import eu.europeana.metis.image.enhancement.ImageEnhancerWorker;
import eu.europeana.metis.image.enhancement.client.ImageEnhancerClient;
import eu.europeana.metis.image.enhancement.config.ImageEnhancerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Paths;
import java.util.Objects;

public class Runner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ImageEnhancerClientConfig imageEnhancerClientConfig;

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
        final BufferedImage outputBufferedImage = imageEnhancerWorker.enhance(sourceFile);

        try {
            ImageIO.write(outputBufferedImage, "png",
                    new File(Paths.get("src", "main", "resources", "img", "europeana_demo_enhanced.png")
                            .toAbsolutePath().toString()));
        } catch (IOException e) {
            LOGGER.error("saving image file", e);
            System.exit(1);
        }
    }
}