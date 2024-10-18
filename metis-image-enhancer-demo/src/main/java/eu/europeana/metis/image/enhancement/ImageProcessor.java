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

  private static final int TIMEOUT = 800;
  private static final int DEMO_ITEMS = 20;
  private static final int WORKER_THREADS = 4;
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final ImageEnhancerWorker imageEnhancerWorker;
  private final ExecutorService pool;

  /**
   * Instantiates a new Image processor.
   *
   * @param imageEnhancerWorker the image enhancer worker
   */
  public ImageProcessor(ImageEnhancerWorker imageEnhancerWorker) {
    this.imageEnhancerWorker = imageEnhancerWorker;
    this.pool = Executors.newFixedThreadPool(WORKER_THREADS);
  }

  /**
   * Process demo.
   */
  public void processDemo() {
    try {
      for (int i = 0; i < DEMO_ITEMS; i++) {
        final int imageIndex = i;
        pool.submit(() -> {
              try {
                processImage(imageIndex);
              } catch (IOException e) {
                LOGGER.error("processing image", e);
              }
            }
        );
      }
    } finally {
      shutdownAndAwaitTermination(pool);
    }
  }

  /**
   * Shutdown and await termination.
   *
   * @param pool the pool
   */
  private void shutdownAndAwaitTermination(ExecutorService pool) {
    pool.shutdown(); // Disable new tasks from being submitted
    try {
      // Wait a while for existing tasks to terminate
      if (!pool.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
        pool.shutdownNow(); // Cancel currently executing tasks
        // Wait a while for tasks to respond to being cancelled
        if (!pool.awaitTermination(TIMEOUT, TimeUnit.SECONDS)) {
          LOGGER.error("Pool did not terminate");
        }
      }
    } catch (InterruptedException ex) {
      // (Re-)Cancel if current thread also interrupted
      pool.shutdownNow();
      // Preserve interrupt status
      Thread.currentThread().interrupt();
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
