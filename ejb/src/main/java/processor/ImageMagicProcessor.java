package processor;

import org.jboss.logging.Logger;

import javax.enterprise.inject.Alternative;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@ImgMagickQualifier
public class ImageMagicProcessor implements ImageProcessor {

    private static final Logger LOGGER = Logger.getLogger(ImageMagicProcessor.class);

    private String imageMagicPath;

    public ImageMagicProcessor(String imageMagicPath) {
        this.imageMagicPath = imageMagicPath;
    }

    @Override
    public File scale(File source, File target, int width, int height) {
        List<String> commands = Arrays.asList(imageMagicPath + "\\convert",
                source.getAbsolutePath(),
                "-resize",
                String.valueOf(width) + "x" + String.valueOf(height) + "!",
                target.getAbsolutePath());
        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(new File(imageMagicPath));
        try {
            Process p = pb.start();
        } catch (IOException e) {
            LOGGER.error("Error at process of cale image with ImageMagick " + e.getMessage());
        }
        return target;
    }
}
