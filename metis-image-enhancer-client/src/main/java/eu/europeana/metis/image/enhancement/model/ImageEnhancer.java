package eu.europeana.metis.image.enhancement;

import java.awt.image.BufferedImage;

/**
 * The interface Thumbnail enhancer.
 */
public interface ImageEnhancer {

    /**
     * Enhance image.
     *
     * @param input the image bytes
     * @return the buffered image
     */
    BufferedImage enhance(byte[] input);

}
