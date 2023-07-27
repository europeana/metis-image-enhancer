package eu.europeana.metis.image.enhancement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * The type Lib Configuration
 */
@Component
public class ImageEnhancerClientConfig {

    @Value("${isr.api.url}")
    private String isrApiUrl;
    @Value("${isr.connect.timeout}")
    private int isrConnectTimeout;
    @Value("${isr.read.timeout}")
    private int isrReadTimeout;

    public String getIsrApiUrl() {
        return isrApiUrl;
    }

    public int getIsrConnectTimeout() {
        return isrConnectTimeout;
    }

    public int getIsrReadTimeout() {
        return isrReadTimeout;
    }
}
