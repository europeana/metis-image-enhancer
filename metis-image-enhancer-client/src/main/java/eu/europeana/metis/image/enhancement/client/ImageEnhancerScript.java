package eu.europeana.metis.image.enhancement.client;

import eu.europeana.metis.image.enhancement.domain.model.ImageEnhancer;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The type Image enhancer script.
 */
public class ImageEnhancerScript implements ImageEnhancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String PYTHON = "python3";
    private final String pathToScript;

    /**
     * Instantiates a new Image enhancer script.
     *
     * @param pathToScript the path to script
     */
    public ImageEnhancerScript(String pathToScript) {
        this.pathToScript = pathToScript;
    }

    private File getTempImageFile(byte[] imageToEnhance, UUID uuid) throws IOException {
        File tempImageFile = File.createTempFile(uuid.toString(), ".img");
        try (FileOutputStream fos = new FileOutputStream(tempImageFile)) {
            fos.write(imageToEnhance);
        } catch (IOException e) {
            LOGGER.error("writing temporary image file", e);
            return null;
        }
        return tempImageFile;
    }

    /**
     * Enhance byte image.
     *
     * @param imageToEnhance the array of bytes of the image to enhance
     * @return the array bytes of the enhanced image
     */
    @Override
    public byte[] enhance(byte[] imageToEnhance) {
        UUID uuid = UUID.randomUUID();
        byte[] enhancedImage = null;

        try {
            File tempImageFile = getTempImageFile(imageToEnhance, uuid);
            if (tempImageFile == null) {
                return new byte[0];
            }
            final String inputFile = tempImageFile.getAbsolutePath();
            final String outputFile = tempImageFile.getAbsolutePath().replace(".img", "_out.img");

            ProcessBuilder processBuilder = new ProcessBuilder(PYTHON, this.pathToScript,
                    "--input", inputFile, "--output", outputFile);
            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            final List<String> results = readProcessOutput(process.getInputStream());
            results.forEach(LOGGER::info);
            final int exitCode = process.waitFor();
            if (exitCode != 0) {
                LOGGER.error("Script execution failed exit code {}", exitCode);
            }

            try (FileInputStream inputStream = new FileInputStream(outputFile)) {
                enhancedImage = inputStream.readAllBytes();
            }
            Files.delete(Paths.get(inputFile));
            Files.delete(Paths.get(outputFile));
            LOGGER.info("temporary files cleanup");
        } catch (IOException e) {
            LOGGER.error("creating temporary image file", e);
        } catch (InterruptedException e) {
            LOGGER.error("in processing", e);
            Thread.currentThread().interrupt();
        }

        return enhancedImage;
    }

    /**
     * Read process output list.
     *
     * @param inputStream the input stream
     * @return the list
     * @throws IOException the io exception
     */
    public List<String> readProcessOutput(InputStream inputStream) throws IOException {
        List<String> stringList = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringList.add(line);
            }
        }
        return stringList;
    }
}
