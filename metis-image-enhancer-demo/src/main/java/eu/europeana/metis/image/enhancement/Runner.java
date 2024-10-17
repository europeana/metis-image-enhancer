package eu.europeana.metis.image.enhancement;

import eu.europeana.metis.image.enhancement.client.ImageEnhancerClient;
import eu.europeana.metis.image.enhancement.config.ImageEnhancerClientConfig;
import eu.europeana.metis.image.enhancement.domain.worker.ImageEnhancerWorker;
import org.springframework.boot.CommandLineRunner;

/**
 * The type Runner.
 */
public class Runner implements CommandLineRunner {

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
  public void run(String[] args) throws InterruptedException {
    ImageProcessor processor = new ImageProcessor(new ImageEnhancerWorker(new ImageEnhancerClient(imageEnhancerClientConfig)));
    processor.processDemo();
  }
}
