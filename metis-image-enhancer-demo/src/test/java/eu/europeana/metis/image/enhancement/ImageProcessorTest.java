package eu.europeana.metis.image.enhancement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.europeana.metis.image.enhancement.domain.model.ImageEnhancer;
import eu.europeana.metis.image.enhancement.domain.worker.ImageEnhancerWorker;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImageProcessorTest {

  @Mock
  private ImageEnhancerWorker worker;

  @InjectMocks
  private ImageProcessor imageProcessor;

  @Test
  void processDemo() {
    when(worker.enhance(any())).thenReturn(new byte[0]);
    imageProcessor.processDemo();
    verify(worker, times(20)).enhance(any());
  }

}
