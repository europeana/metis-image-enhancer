package eu.europeana.metis.image.enhancement;

import eu.europeana.metis.image.enhancement.config.ImageEnhancerClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Value("${isr.api.url}")
    private String isrApiUrl;
    @Value("${isr.connect.timeout}")
    private int isrConnectTimeout;
    @Value("${isr.read.timeout}")
    private int isrReadTimeout;

    @Bean
    public ImageEnhancerClientConfig getImageEnhancerClientConfig() {
        return new ImageEnhancerClientConfig(isrApiUrl, isrConnectTimeout, isrReadTimeout);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ImageEnhancerClientConfig imageEnhancerClientConfig) {
        return new Runner(imageEnhancerClientConfig);
    }

}
