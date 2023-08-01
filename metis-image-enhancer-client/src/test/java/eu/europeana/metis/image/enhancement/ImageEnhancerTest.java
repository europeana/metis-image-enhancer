package eu.europeana.metis.image.enhancement;

import eu.europeana.metis.image.enhancement.model.ImageEnhancer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageEnhancerTest {

    @Mock
    private ImageEnhancer enhancer;

    @InjectMocks
    private ImageEnhancerWorker imageEnhancerWorker;

    @Test
    void givenThumbnailFile_thenSuccess() throws IOException, URISyntaxException {
        // given
        final String testInputFile =
                Paths.get(requireNonNull(ImageEnhancerTest.class.getClassLoader().getResource("img/thumbnail.jpg")).toURI()
                ).toFile().getAbsolutePath();

        final BufferedImage bufferedImage = ImageIO.read(new File(testInputFile));
        when(enhancer.enhance(any(byte[].class))).thenReturn(bufferedImage);

        // when
        final BufferedImage output = imageEnhancerWorker.enhance(new byte[]{});

        // then
        assertNotNull(output);
    }
}
