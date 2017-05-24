package canvas;

import model.xml.Project;
import org.faceless.pdf2.*;
import org.jboss.logging.Logger;
import repository.ProjectRepository;
import util.ProjectUtils;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.apache.commons.io.IOUtils.closeQuietly;

@BFOQualifier
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
        try (FileInputStream fis = new FileInputStream(file)){

            PDFImage image = new PDFImage(fis);
            float width = image.getWidth();
            float height = image.getHeight();
            canvas.drawImage(image, x, y, width, height);
        } catch (IOException e) {
            LOGGER.error("Error at process of draw image" + e.getMessage());
        }
    }

    @Override
    public void build(ProjectRepository repository) {
          page.drawCanvas(canvas, 0, 0, canvas.getWidth(), canvas.getHeight());
            page.flush();
        try (FileOutputStream stream = new FileOutputStream(repository.getPreview(ProjectUtils.PREVIEW_TYPES.PDF))){
            pdf.render(stream);
            closeQuietly(stream);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
