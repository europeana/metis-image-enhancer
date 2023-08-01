package eu.europeana.metis.image.enhancement.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * The type Lib Configuration
 */
public class ImageEnhancerClientConfig {

    private final String isrApiUrl;
    private final int isrConnectTimeout;
    private final int isrReadTimeout;

    public ImageEnhancerClientConfig(String url,
                                     int connectTimeout,
                                     int readTimeout) {
        this.isrApiUrl = url;
        this.isrConnectTimeout = connectTimeout;
        this.isrReadTimeout = readTimeout;
    }

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
