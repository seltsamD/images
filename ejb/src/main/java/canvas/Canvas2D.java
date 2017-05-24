package canvas;

import model.xml.Project;
import org.jboss.logging.Logger;
import repository.ProjectRepository;
import util.ProjectUtils;

import javax.enterprise.inject.Alternative;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

import static java.awt.Color.BLACK;

@Canvas2DQualifier
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


    @Override
    public void build(ProjectRepository repository) {
        try {
            ImageIO.write(image, "png", new FileOutputStream(repository.getPreview(ProjectUtils.PREVIEW_TYPES.PNG)));
        } catch (IOException e) {
            LOGGER.error("Error at process of build preview " + e.getMessage());
        }
    }

}
