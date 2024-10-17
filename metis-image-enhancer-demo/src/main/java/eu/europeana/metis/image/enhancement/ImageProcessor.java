package eu.europeana.metis.image.enhancement;

import eu.europeana.metis.image.enhancement.domain.worker.ImageEnhancerWorker;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Image processor.
 */
public class ImageProcessor {

  public static final int TIMEOUT = 800;
  public static final int DEMO_ITEMS = 20;
  public static final int WORKER_THREADS = 4;
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final ImageEnhancerWorker imageEnhancerWorker;

  /**
   * Instantiates a new Image processor.
   *
   * @param imageEnhancerWorker the image enhancer worker
   */
  public ImageProcessor(ImageEnhancerWorker imageEnhancerWorker) {
    this.imageEnhancerWorker = imageEnhancerWorker;
  }

  /**
   * Process demo.
   *
   * @throws InterruptedException the interrupted exception
   */
  public void processDemo() throws InterruptedException {
    ExecutorService service = Executors.newFixedThreadPool(WORKER_THREADS);
    try {
      for (int i = 0; i < DEMO_ITEMS; i++) {
        final int imageIndex = i;
        service.submit(() -> {
              try {
                processImage(imageIndex);
              } catch (IOException e) {
                LOGGER.error("processing image", e);
              }
            }
        );
      }
    } finally {
      service.shutdown();
      service.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
    }
  }

  /**
   * Process image.
   *
   * @param indexOfImage the index of Image
   * @throws IOException the io exception
   */
  private void processImage(int indexOfImage) throws IOException {
    byte[] sourceFile;
    try (InputStream inputStream = new FileInputStream(
        Paths.get("src", "main", "resources", "img", "item" + indexOfImage + ".jpg").toAbsolutePath()
             .toString())) {
      sourceFile = Objects.requireNonNull(inputStream).readAllBytes();
    }

    try {
      Files.write(Paths.get("src", "main", "resources", "img", "item" + indexOfImage + "_enhanced.jpg")
                       .toAbsolutePath(), imageEnhancerWorker.enhance(sourceFile), StandardOpenOption.CREATE);
    } catch (IOException e) {
      LOGGER.error("saving image output file", e);
    }
  }
}
