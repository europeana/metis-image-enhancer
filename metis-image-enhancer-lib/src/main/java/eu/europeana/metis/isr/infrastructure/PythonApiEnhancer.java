package eu.europeana.metis.isr.infrastructure;

import eu.europeana.metis.isr.domain.model.ThumbnailEnhancer;
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
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Objects;

/**
 * The type Python api enhancer.
 */
public class PythonApiEnhancer implements ThumbnailEnhancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(PythonApiEnhancer.class);
    private final String apiURL;
    private final Integer connectTimeOut;
    private final Integer readTimeOut;


    /**
     * Instantiates a new Python api enhancer.
     *
     * @param apiURL         the api url
     * @param connectTimeOut the connect time out
     * @param readTimeOut    the read time out
     */
    public PythonApiEnhancer(String apiURL, Integer connectTimeOut, Integer readTimeOut) {
        this.apiURL = apiURL;
        this.connectTimeOut = connectTimeOut;
        this.readTimeOut = readTimeOut;
    }

    @Override
    public BufferedImage enhance(String inputFile) {
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
        body.add("image", new FileSystemResource(new File(inputFile)));

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
