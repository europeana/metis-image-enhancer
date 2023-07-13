package eu.europeana.metis.isr.domain.service;

import eu.europeana.metis.isr.domain.model.ThumbnailEnhancer;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * The type Super resolution service.
 */
public class ImageSuperResolutionService {

    private final ThumbnailEnhancer thumbnailEnhancer;

    /**
     * Instantiates a new Super resolution service.
     *
     * @param thumbnailEnhancer the thumbnail enhancer
     */
    public ImageSuperResolutionService(ThumbnailEnhancer thumbnailEnhancer) {
        this.thumbnailEnhancer = thumbnailEnhancer;
    }

    /**
     * Enhance thumbnail file
     *
     * @param input the input file path
     * @return output file path
     */
    public BufferedImage enhance(String input) {
        // add here pre-conditions
        final BufferedImage image = thumbnailEnhancer.enhance(input);
        // add here post-conditions
        return sharpen(image);
    }

    private BufferedImage sharpen(BufferedImage sharpenImage) {
        Kernel kernel = new Kernel(3, 3,
                new float[] { 0, -1, 0,
                        -1, 5, -1,
                        0, -1, 0});
        BufferedImageOp op = new ConvolveOp(kernel);
        sharpenImage = op.filter(sharpenImage, null);
        return sharpenImage;
    }
}
