package eu.europeana.metis.isr.library;

import eu.europeana.metis.isr.configuration.LibConfiguration;
import eu.europeana.metis.isr.domain.service.ImageSuperResolutionService;
import eu.europeana.metis.isr.infrastructure.PythonApiEnhancer;

/**
 * The type Super resolution library service.
 */
public class ImageSuperResolutionLibrary {

    private ImageSuperResolutionLibrary() {
    }

    /**
     * Gets service.
     *
     * @param serviceType the service type
     * @return the service
     */
    public static ImageSuperResolutionService getService(LibServiceType serviceType) {
        LibConfiguration libConfiguration = new LibConfiguration();
        final String apiUrl = libConfiguration.getApiUrl();
        final Integer connectTimeOut = libConfiguration.getConnectTimeout();
        final Integer readTimeOut = libConfiguration.getReadTimeout();
        if (serviceType.equals(LibServiceType.API)) {
            return new ImageSuperResolutionService(
                    new PythonApiEnhancer(apiUrl, connectTimeOut, readTimeOut));
        } else {
            throw new IllegalArgumentException("Invalid service type");
        }
    }
}
