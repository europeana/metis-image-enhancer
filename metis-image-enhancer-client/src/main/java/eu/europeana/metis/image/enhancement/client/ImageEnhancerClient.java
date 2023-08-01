package eu.europeana.metis.image.enhancement.client;

import eu.europeana.metis.image.enhancement.config.ImageEnhancerClientConfig;
import eu.europeana.metis.image.enhancement.domain.model.ImageEnhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Objects;

/**
 * The type Python api enhancer.
 */
public class ImageEnhancerClient implements ImageEnhancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final String apiURL;
    private final Integer connectTimeOut;
    private final Integer readTimeOut;

    /**
     * Constructor
     *
     * @param imageEnhancerClientConfig the image enhancer configuration
     */
    public ImageEnhancerClient(ImageEnhancerClientConfig imageEnhancerClientConfig) {
        this.apiURL = imageEnhancerClientConfig.getIsrApiUrl();
        this.connectTimeOut = imageEnhancerClientConfig.getIsrConnectTimeout();
        this.readTimeOut = imageEnhancerClientConfig.getIsrReadTimeout();
    }

    @Override
    public byte[] enhance(byte[] imageToEnhance) {
        final URI uri;
        try {
            uri = new URI(apiURL + "/enhance/image");
        } catch (URISyntaxException e) {
            LOGGER.error("Error with API URL", e);
            return null;
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        final HttpEntity<byte[]> httpEntity = new HttpEntity<>(imageToEnhance, headers);

        final RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(this.connectTimeOut))
                .setReadTimeout(Duration.ofSeconds(this.readTimeOut))
                .build();
        final ResponseEntity<byte[]> response = restTemplate.postForEntity(uri, httpEntity, byte[].class);

        ByteArrayInputStream responseByteArrayStream = null;
        if (response.getStatusCode().is2xxSuccessful()) {
            if (response.getHeaders().containsKey("Elapsed-Time")) {
                LOGGER.info("Image processed successfully! Elapsed Time: {}", response.getHeaders().get("Elapsed-Time"));
            } else {
                LOGGER.info("Image processed successfully!");
            }
            responseByteArrayStream = new ByteArrayInputStream(Objects.requireNonNull(response.getBody()));
        } else {
            LOGGER.error("Failed to process image. Response code: {}", response.getStatusCodeValue());
        }

        return responseByteArrayStream.readAllBytes();
    }
}
