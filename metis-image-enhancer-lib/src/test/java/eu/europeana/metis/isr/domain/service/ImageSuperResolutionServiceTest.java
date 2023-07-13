package eu.europeana.metis.isr.domain.service;

import eu.europeana.metis.isr.domain.model.ThumbnailEnhancer;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageSuperResolutionServiceTest {

    @Mock
    private ThumbnailEnhancer enhancer;

    @InjectMocks
    private ImageSuperResolutionService service;

    @Test
    void givenThumbnailFile_thenSuccess() throws IOException, URISyntaxException {
        // given
        final String testInputFile =
                Paths.get(
                        ImageSuperResolutionServiceTest.class.getClassLoader().getResource("img/thumbnail.jpg").toURI()
                ).toFile().getAbsolutePath();
        final BufferedImage bufferedImage = ImageIO.read(new File(testInputFile));
        when(enhancer.enhance(anyString())).thenReturn(bufferedImage);

        // when
        final BufferedImage output = service.enhance("file://thumbnail.extension");

        // then
        assertNotNull(output);
    }
}
