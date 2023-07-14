package eu.europeana.metis.isr;

import eu.europeana.metis.isr.domain.service.ImageSuperResolutionService;
import eu.europeana.metis.isr.library.ImageSuperResolutionLibrary;
import eu.europeana.metis.isr.library.LibServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * The type Main.
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        final ImageSuperResolutionService service = ImageSuperResolutionLibrary.getService(LibServiceType.API);

        final BufferedImage outputBufferedImage = service.enhance(
                Paths.get("src", "main", "resources", "img", "europeana_demo.png")
                        .toAbsolutePath()
                        .toString());

        try {
            ImageIO.write(outputBufferedImage, "png",
                    new File(Paths.get("src", "main", "resources", "img", "europeana_demo_enhanced.png")
                            .toAbsolutePath()
                            .toString()));
        } catch (IOException e) {
            LOGGER.error("saving image file", e);
            System.exit(1);
        }
    }
}
