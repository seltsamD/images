package processor;

import org.apache.commons.io.FileUtils;
import service.ConfigService;
import util.PropertyKeys;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

@Alternative
public class ImageMagicProcessor implements ImageProcessor {

    String imageMagicPath;

    public ImageMagicProcessor(String imageMagicPath) {
        this.imageMagicPath = imageMagicPath;
    }

    @Override
    public File scale(File source, File target, int width, int height) {

        String comand = imageMagicPath + "\\convert";
        String sourcePath = source.getAbsolutePath();
        String targetPath = target.getAbsolutePath();
        String com = "-resize";
        String scale = String.valueOf(width)+"x"+String.valueOf(height)+"!";
        ProcessBuilder pb = new ProcessBuilder(comand, sourcePath, com, scale, targetPath);
        pb.directory(new File(imageMagicPath));
        try {
            Process p = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return target;
    }
}
