package eu.europeana.metis.image.enhancement.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.http.JvmProxyConfigurer;
import eu.europeana.metis.image.enhancement.config.ImageEnhancerClientConfig;
import java.io.IOException;
import java.util.Objects;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class ImageEnhancerClientTest {

  private static WireMockServer wireMockServer;
  ImageEnhancerClient client;

  @BeforeAll
  static void createWireMock() {
    wireMockServer = new WireMockServer(wireMockConfig()
        .dynamicPort()
        .enableBrowserProxying(true)
        .notifier(new ConsoleNotifier(true)));
    wireMockServer.start();

    JvmProxyConfigurer.configureFor(wireMockServer);
  }

  @Test
  void enhance() throws IOException {
    final String successResponse = new String(
        Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("img/thumbnail.jpg"))
               .readAllBytes());
    wireMockServer.stubFor(post("/")
        .withHost(equalTo("enhance.host"))
        .willReturn(aResponse()
            .withHeader("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE)
            .withBody(successResponse)
            .withStatus(200)));
    ImageEnhancerClientConfig clientConfig = new ImageEnhancerClientConfig("http://enhance.host", 300, 300);
    client = new ImageEnhancerClient(clientConfig);
    final byte[] enhanced = client.enhance("test".getBytes());
    assertNotNull(enhanced);
  }

  @Test
  void enhance_uri_error() {
    ImageEnhancerClientConfig clientConfig = new ImageEnhancerClientConfig("http:/enhanc host", 300, 300);
    client = new ImageEnhancerClient(clientConfig);
    final byte[] enhanced = client.enhance("test".getBytes());
    assertNotNull(enhanced);
  }

  @Test
  void enhance_server_error() throws IOException {
    final String successResponse = new String(
        Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("img/thumbnail.jpg"))
               .readAllBytes());
    wireMockServer.stubFor(post("/")
        .withHost(equalTo("enhance.host"))
        .willReturn(aResponse()
            .withHeader("Content-Type", MediaType.APPLICATION_OCTET_STREAM_VALUE)
            .withBody(successResponse)
            .withStatus(500)));
    ImageEnhancerClientConfig clientConfig = new ImageEnhancerClientConfig("http://enhance.host", 300, 300);
    client = new ImageEnhancerClient(clientConfig);
    final byte[] enhanced = client.enhance("test".getBytes());
    assertNotNull(enhanced);
  }
}
