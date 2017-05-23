package canvas;

import model.xml.Project;
import org.jboss.logging.Logger;

import javax.enterprise.inject.Alternative;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import static java.awt.Color.BLACK;

//TODO: remove Alternative and create qualifier for one of Canvas implementations
@Alternative
public class Canvas2D implements Canvas {

    private static final Logger LOGGER = Logger.getLogger(Canvas2D.class);
    private Graphics2D graphics;
    private BufferedImage image;

    public Canvas2D() {
    }


    @Override
    public void prepare(Project project) {
        this.image = new BufferedImage(project.getWidth(), project.getHeight(), BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
    }

    @Override
    public void drawText(int x, int y, String value) {
        graphics.setPaint(BLACK);
        graphics.drawString(value, x, y);

    }

    @Override
    public void drawImage(int x, int y, File file) {
        try {
            BufferedImage buf = ImageIO.read(file);
            graphics.drawImage(buf, x, y, buf.getWidth(), buf.getHeight(), null);
        } catch (IOException e) {
            LOGGER.error("Error at process of draw image " + e.getMessage());
        }
    }

    //TODO: set first argument ProjectRepository
    // after that you could just call
    // ImageIO.write(image, "png", new FileOutputStream(repo.getPreview(PNG_TYPE)));
    @Override
    public void build(File file) {
        try {
            File result = Paths.get(file.toURI()).resolve("preview.png").toFile();
            ImageIO.write(image, "png", new FileOutputStream(result));
        } catch (IOException e) {
            LOGGER.error("Error at process of build preview " + e.getMessage());
        }
    }

}
