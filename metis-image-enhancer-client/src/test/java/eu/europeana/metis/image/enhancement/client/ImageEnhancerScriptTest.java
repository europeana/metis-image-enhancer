package eu.europeana.metis.image.enhancement.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImageEnhancerScriptTest {

  ImageEnhancerScript script;

  @Test
  void enhance() {
    script = spy(new ImageEnhancerScript(""));
    script.enhance("info".getBytes(StandardCharsets.UTF_8));
    verify(script, times(1)).enhance(any());
  }

  @Test
  void readProcessOutput() throws IOException {
    script = new ImageEnhancerScript("");
    ByteArrayInputStream bais = new ByteArrayInputStream("success\r\nexpected".getBytes(StandardCharsets.UTF_8));
    List<String> result = script.readProcessOutput(bais);
    assertEquals("success,expected", String.join(",", result));
  }
}
