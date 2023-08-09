package eu.europeana.metis.image.enhancement;

import eu.europeana.metis.image.enhancement.config.ImageEnhancerClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Application configuration.
 */
@Configuration
public class ApplicationConfiguration {

    @Value("${isr.api.url}")
    private String apiURL;
    @Value("${isr.connect.timeout}")
    private int connectTimeout;
    @Value("${isr.read.timeout}")
    private int readTimeout;
    @Value("${isr.script}")
    private String pathToScript;

    /**
     * Gets image enhancer client configuration.
     *
     * @return the image enhancer client configuration
     */
    @Bean
    public ImageEnhancerClientConfig getImageEnhancerClientConfig() {
        return new ImageEnhancerClientConfig(apiURL, connectTimeout, readTimeout);
    }

    /**
     * Command line runner for api.
     *
     * @param imageEnhancerClientConfig the image enhancer client configuration
     * @return the command line runner
     */
//    @Bean(name = "apiCommandLineRunner")
//    @ConditionalOnProperty(prefix = "worker", name = "service.type", havingValue = "api")
//    public CommandLineRunner commandLineRunner(ImageEnhancerClientConfig imageEnhancerClientConfig) {
//        return new Runner(imageEnhancerClientConfig);
//    }

    /**
     * Command line runner for script.
     *
     * @return the command line runner
     */
    @Bean(name = "scriptCommandLineRunner")
    @ConditionalOnProperty(prefix = "worker", name = "service.type", havingValue = "script", matchIfMissing = true)
    public CommandLineRunner commandLineRunner() {
        return new ScriptRunner( pathToScript);
    }
}
