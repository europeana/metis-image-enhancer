package eu.europeana.metis.image.enhancement;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * Image enhancer worker
 */
public class ImageEnhancerWorker {

    private final ImageEnhancer imageEnhancer;

    /**
     * Instantiates a new Super resolution service.
     *
     * @param imageEnhancer the thumbnail enhancer
     */
    public ImageEnhancerWorker(ImageEnhancer imageEnhancer) {
        this.imageEnhancer = imageEnhancer;
    }

    /**
     * Enhance image
     *
     * @param input the input image
     * @return output buffered image
     */
    public BufferedImage enhance(byte[] input) {
        final BufferedImage image = imageEnhancer.enhance(input);
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
