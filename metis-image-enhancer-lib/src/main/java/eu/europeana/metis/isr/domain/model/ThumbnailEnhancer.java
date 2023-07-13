package eu.europeana.metis.isr.domain.model;

import java.awt.image.BufferedImage;

/**
 * The interface Thumbnail enhancer.
 */
public interface ThumbnailEnhancer {

    /**
     * Enhance buffered image.
     *
     * @param input the path file for image input
     * @return the buffered image
     */
    BufferedImage enhance(String input);

}
