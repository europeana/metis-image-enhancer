package eu.europeana.metis.image.enhancement;

import eu.europeana.metis.image.enhancement.domain.worker.ImageEnhancerWorker;
import eu.europeana.metis.image.enhancement.client.ImageEnhancerScript;
import org.springframework.boot.CommandLineRunner;

/**
 * The type Script runner.
 */
public class ScriptRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws InterruptedException {
        ImageProcessor processor = new ImageProcessor(new ImageEnhancerWorker(new ImageEnhancerScript()));
        processor.processDemo();
    }
}
