package eu.europeana.metis.image.enhancement.model;

import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * The interface Thumbnail enhancer.
 */
public interface ImageEnhancer {

    /**
     * Enhance buffered image.
     *
     * @param imageToEnhance the image to enhance
     * @return the buffered image
     */
    BufferedImage enhance(InputStream imageToEnhance);

    /**
     * Enhance byte image.
     *
     * @param imageToEnhance the array of bytes of the image to enhance
     * @return the array bytes of the enhanced image
     */
    byte[] enhance(byte[] imageToEnhance);
}
