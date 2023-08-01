package eu.europeana.metis.image.enhancement.domain.worker;

import eu.europeana.metis.image.enhancement.domain.model.ImageEnhancer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageEnhancerWorkerTest {

    @Mock
    private ImageEnhancer enhancer;

    @InjectMocks
    private ImageEnhancerWorker imageEnhancerWorker;

    @Test
    void givenThumbnailFile_thenSuccess() throws IOException, URISyntaxException {
        // given
        final String testInputFile =
                Paths.get(requireNonNull(ImageEnhancerWorkerTest.class.getClassLoader().getResource("img/thumbnail.jpg")).toURI()
                ).toFile().getAbsolutePath();

        try(FileInputStream fileInputStream = new FileInputStream(testInputFile)) {
            final byte[] byteImage = fileInputStream.readAllBytes();
            when(enhancer.enhance(any(byte[].class))).thenReturn(byteImage);
        }

        // when
        final byte[] output = imageEnhancerWorker.enhance(new byte[]{});

        // then
        assertNotNull(output);
    }
}
