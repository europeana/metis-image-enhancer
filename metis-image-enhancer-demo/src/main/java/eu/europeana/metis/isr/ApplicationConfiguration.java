package eu.europeana.metis.isr;

import eu.europeana.metis.image.enhancement.config.ImageEnhancerClientConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public ImageEnhancerClientConfig getImageEnhancerClientConfig() {
        return new ImageEnhancerClientConfig();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ImageEnhancerClientConfig imageEnhancerClientConfig) {
        return new Runner(imageEnhancerClientConfig);
    }

}