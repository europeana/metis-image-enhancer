package eu.europeana.metis.isr.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * The type Lib Configuration
 */
public class LibConfiguration {

    private static final String PROPERTIES_FILE = "metis-isr.properties";
    private static final Logger LOGGER = LoggerFactory.getLogger(LibConfiguration.class);
    private final Properties properties = new Properties();

    /**
     * Instantiates a new Lib configuration.
     */
    public LibConfiguration() {
        try (InputStream propertyStream = getClass().getClassLoader().getResourceAsStream((PROPERTIES_FILE))) {
            properties.load(propertyStream);
        } catch (IOException ex) {
            LOGGER.error("Unable to load configuration properties", ex);
        }
    }

    /**
     * Gets api url.
     *
     * @return the api url
     */
    public String getApiUrl() {
        return properties.getProperty("api.url");
    }

    /**
     * Gets read timeout.
     *
     * @return the read timeout
     */
    public Integer getReadTimeout() { return Integer.valueOf(properties.getProperty("read.timeout","300"));}

    /**
     * Gets connect timeout.
     *
     * @return the connect timeout
     */
    public Integer getConnectTimeout() { return Integer.valueOf(properties.getProperty("connect.timeout","300"));}
}
