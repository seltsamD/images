package canvas;

import model.xml.Project;
import org.faceless.pdf2.*;
import org.jboss.logging.Logger;

import javax.enterprise.inject.Alternative;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

//TODO: remove Alternative and create qualifier for one of Canvas implementations
@Alternative
public class CanvasBFO implements Canvas {
    private PDF pdf;
    private PDFPage page;
    private PDFCanvas canvas;
    private static final Logger LOGGER = Logger.getLogger(CanvasBFO.class);

    @Override
    public void prepare(Project project) {
        pdf = new PDF();
        page = pdf.newPage(project.getHeight(), project.getWidth());
        canvas = new PDFCanvas(page.getWidth(), page.getHeight());
        canvas.flush();
    }

    @Override
    public void drawText(int x, int y, String value) {
        PDFStyle style = new PDFStyle();
        style.setFont(new StandardFont(StandardFont.COURIER), 20);
        style.setFillColor(Color.BLACK);
        page.setStyle(style);
        page.drawText(value, x, y);
    }

    @Override
    public void drawImage(int x, int y, File file) {
        try {

            PDFImage image = new PDFImage(new FileInputStream(file));
            float width = image.getWidth();
            float height = image.getHeight();
            canvas.drawImage(image, x, y, width, height);
            canvas.flush();
        } catch (IOException e) {
            LOGGER.error("Error at process of draw image" + e.getMessage());
        }
    }

    //TODO: set first argument ProjectRepository
    // after that you could just call
    // pdf.render(new FileOutputStream(repo.getPreview(PDF_TYPE));
    @Override
    public void build(File file) {
        try (FileOutputStream stream = new FileOutputStream(Paths.get(file.toURI()).resolve("preview.pdf").toFile())) {
            page.drawCanvas(canvas, 0, 0, canvas.getWidth(), canvas.getHeight());
            pdf.render(stream);
            //TODO: pdf objects its some kind of closable it is important to close() it
            // use IOUtils.closeQuietly();
        } catch (IOException e) {
            LOGGER.error("Error at process of build preview PDF" + e.getMessage());
        }
    }
}
