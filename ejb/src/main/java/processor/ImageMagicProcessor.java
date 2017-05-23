package processor;

import org.jboss.logging.Logger;

import javax.enterprise.inject.Alternative;
import java.io.File;
import java.io.IOException;

//TODO: remove Alternative and create qualifier for one of Canvas implementations
@Alternative
public class ImageMagicProcessor implements ImageProcessor {

    private static final Logger LOGGER = Logger.getLogger(ImageMagicProcessor.class);

    private String imageMagicPath;

    public ImageMagicProcessor(String imageMagicPath) {
        this.imageMagicPath = imageMagicPath;
    }

    @Override
    public File scale(File source, File target, int width, int height) {
        //TODO: replace next lines with Arrays.asList()
        // it will be more readable
        /*List<String> command = Arrays.asList(
                "convert",
                "file.png",
                "file.jpg"
        );*/
        String command = imageMagicPath + "\\convert";
        String sourcePath = source.getAbsolutePath();
        String targetPath = target.getAbsolutePath();
        String com = "-resize";
        String scale = String.valueOf(width) + "x" + String.valueOf(height) + "!";
        ProcessBuilder pb = new ProcessBuilder(command, sourcePath, com, scale, targetPath);
        pb.directory(new File(imageMagicPath));
        try {
            Process p = pb.start();
        } catch (IOException e) {
            LOGGER.error("Error at process of cale image with ImageMagick " + e.getMessage());
        }
        return target;
    }
}
