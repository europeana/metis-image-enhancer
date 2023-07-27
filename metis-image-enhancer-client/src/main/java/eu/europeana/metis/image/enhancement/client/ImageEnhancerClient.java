package eu.europeana.metis.image.enhancement.client;

import eu.europeana.metis.image.enhancement.ImageEnhancer;
import eu.europeana.metis.image.enhancement.config.ImageEnhancerClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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
     * @param imageEnhancerClientConfig the image enhancer configuration
     */
    public ImageEnhancerClient(ImageEnhancerClientConfig imageEnhancerClientConfig) {
        this.apiURL = imageEnhancerClientConfig.getIsrApiUrl();
        this.connectTimeOut = imageEnhancerClientConfig.getIsrConnectTimeout();
        this.readTimeOut = imageEnhancerClientConfig.getIsrReadTimeout();
    }

    @Override
    public BufferedImage enhance(byte[] input) {
        final URI uri;
        try {
            uri = new URI(apiURL + "/enhance/image");
        } catch (URISyntaxException e) {
            LOGGER.error("Error with API URL", e);
            return null;
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create the request body as a MultiValueMap
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", input);

        final HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        final RestTemplate restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofSeconds(this.connectTimeOut))
                .setReadTimeout(Duration.ofSeconds(this.readTimeOut))
                .build();
        final ResponseEntity<byte[]> response = restTemplate.postForEntity(uri, httpEntity, byte[].class);

        BufferedImage outputBufferedImage = null;
        if (response.getStatusCode().is2xxSuccessful()) {
            LOGGER.info("Image processed successfully!");
            try {
                outputBufferedImage = ImageIO.read(new ByteArrayInputStream(Objects.requireNonNull(response.getBody())));
            } catch (IOException e) {
                LOGGER.error("processing image", e);
            }
        } else {
            LOGGER.error("Failed to process image. Response code: {}", response.getStatusCodeValue());
        }

        return outputBufferedImage;
    }
}
