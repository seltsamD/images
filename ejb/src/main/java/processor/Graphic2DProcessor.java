package processor;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Graphic2DProcessor implements ImageProcessor{
    @Override
    public File scale(File source, File target, int width, int height) {
        try {
            BufferedImage buf = ImageIO.read(source);
            BufferedImage outputImage = new BufferedImage(width, height, buf.getType());
            Graphics2D graphics2D = outputImage.createGraphics();
            graphics2D.drawImage(buf, 0, 0, width, height, null);
            graphics2D.dispose();
            ImageIO.write(outputImage, FilenameUtils.getExtension(source.getName()), target);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return target;
    }
}
