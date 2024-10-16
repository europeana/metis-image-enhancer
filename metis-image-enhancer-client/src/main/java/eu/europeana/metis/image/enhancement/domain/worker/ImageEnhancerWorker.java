package eu.europeana.metis.image.enhancement.domain.worker;

import eu.europeana.metis.image.enhancement.domain.model.ImageEnhancer;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Image enhancer worker
 */
public class ImageEnhancerWorker {

  private static final Kernel KERNEL = new Kernel(3, 3,
      new float[]{-0.0023F, -0.0432F, -0.0023F,
          -0.0432F, 1.182F, -0.0432F,
          -0.0023F, -0.0432F, -0.0023F});
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final boolean IS_FILTER_ENABLED = false;
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
   * Get the image format 'jpeg/png/bmp/tif' if not found png is returned by default
   *
   * @param byte array that contains image information
   * @return format name
   */
  private static String getImageFormat(byte[] input) {
    String format = "png";
    try (ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(input))) {

      Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);

      if (readers.hasNext()) {
        ImageReader reader = readers.next();
        format = reader.getFormatName();
      }
    } catch (IOException e) {
      LOGGER.error("Determining image format", e);
    }
    return format;
  }

  /**
   * Enhance image
   *
   * @param input the input array bytes of an image
   * @return output byte array bytes of an image
   */
  public byte[] enhance(byte[] input) {
    final BufferedImage image;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageEnhancer.enhance(input));
      image = ImageIO.read(byteArrayInputStream);

      final String format = getImageFormat(input);

      if (IS_FILTER_ENABLED) {
        ImageIO.write(sharpen(image), format, baos);
      }
      ImageIO.write(image, format, baos);
    } catch (IOException e) {
      LOGGER.error("enhancing the image", e);
    }

    return baos.toByteArray();
  }

  /**
   * Sharpen blurry image after AI enhancement
   *
   * @param imageToSharpen image to be sharpened
   * @return BufferedImage sharpened image
   */
  private BufferedImage sharpen(BufferedImage imageToSharpen) {
    BufferedImageOp op = new ConvolveOp(KERNEL);
    imageToSharpen = op.filter(imageToSharpen, null);
    return imageToSharpen;
  }
}
