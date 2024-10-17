package eu.europeana.metis.image.enhancement;

import eu.europeana.metis.image.enhancement.domain.worker.ImageEnhancerWorker;
import eu.europeana.metis.image.enhancement.client.ImageEnhancerScript;
import org.springframework.boot.CommandLineRunner;

/**
 * The type Script runner.
 */
public class ScriptRunner implements CommandLineRunner {

    private final ImageEnhancerScript enhancerScript;

    /**
     * Instantiates a new Script runner.
     *
     * @param pathToScript the path to script
     */
    public ScriptRunner(String pathToScript) {
        this.enhancerScript = new ImageEnhancerScript(pathToScript);
    }

    @Override
    public void run(String[] args) throws InterruptedException {
        ImageProcessor processor = new ImageProcessor(new ImageEnhancerWorker(enhancerScript));
        processor.processDemo();
    }
}
