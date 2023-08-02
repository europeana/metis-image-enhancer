package eu.europeana.metis.image.enhancement.config;

/**
 * The type Lib Configuration
 */
public class ImageEnhancerClientConfig {

    private final String isrApiUrl;
    private final int isrConnectTimeout;
    private final int isrReadTimeout;

    /**
     * Instantiates a new Image enhancer client configuration.
     *
     * @param url            the url
     * @param connectTimeout the connect timeout
     * @param readTimeout    the read timeout
     */
    public ImageEnhancerClientConfig(String url,
                                     int connectTimeout,
                                     int readTimeout) {
        this.isrApiUrl = url;
        this.isrConnectTimeout = connectTimeout;
        this.isrReadTimeout = readTimeout;
    }

    /**
     * Gets isr api url.
     *
     * @return the isr api url
     */
    public String getIsrApiUrl() {
        return isrApiUrl;
    }

    /**
     * Gets isr connect timeout.
     *
     * @return the isr connect timeout
     */
    public int getIsrConnectTimeout() {
        return isrConnectTimeout;
    }

    /**
     * Gets isr read timeout.
     *
     * @return the isr read timeout
     */
    public int getIsrReadTimeout() {
        return isrReadTimeout;
    }
}
