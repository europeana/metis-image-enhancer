package eu.europeana.metis.isr;

import eu.europeana.metis.isr.domain.service.ImageSuperResolutionService;
import eu.europeana.metis.isr.library.ImageSuperResolutionLibrary;
import eu.europeana.metis.isr.library.LibServiceType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * The type Main.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        ImageSuperResolutionService service = ImageSuperResolutionLibrary.getService(LibServiceType.API);

        BufferedImage outputBufferedImage = service.enhance(Paths.get("demo","src", "main", "resources", "img", "europeana_demo.png").toAbsolutePath().toString());
        try {
            ImageIO.write(outputBufferedImage , "png", new File(Paths.get("demo","src", "main", "resources", "img", "europeana_demo_enhanced.png").toAbsolutePath().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
