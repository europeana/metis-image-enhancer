package eu.europeana.metis.isr.library;

import eu.europeana.metis.isr.domain.service.ImageSuperResolutionService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ImageSuperResolutionLibraryTest {

    @Test
    void givenObject_whenGetService_thenSuccess() {
        // given
        ImageSuperResolutionService service;

        //when
        service = ImageSuperResolutionLibrary.getService(LibServiceType.API);

        // then
        assertNotNull(service);
    }

}
