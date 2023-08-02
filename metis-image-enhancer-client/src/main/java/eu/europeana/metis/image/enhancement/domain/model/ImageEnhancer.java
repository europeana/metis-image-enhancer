package eu.europeana.metis.image.enhancement.domain.model;

/**
 * The interface Thumbnail enhancer.
 */
public interface ImageEnhancer {


    /**
     * Enhance byte image.
     *
     * @param imageToEnhance the array of bytes of the image to enhance
     * @return the array bytes of the enhanced image
     */
    byte[] enhance(byte[] imageToEnhance);
}
