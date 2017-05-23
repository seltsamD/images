package processor;

import org.apache.commons.io.FilenameUtils;
import org.jboss.logging.Logger;

import javax.enterprise.inject.Alternative;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//TODO: remove Alternative and create qualifier for one of Canvas implementations
@Alternative
public class Graphic2DProcessor implements ImageProcessor {

    private static final Logger LOGGER = Logger.getLogger(Graphic2DProcessor.class);

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
            LOGGER.error("Error at process of scale image with Graphic2d " + e.getMessage());
        }

        return target;
    }
}
