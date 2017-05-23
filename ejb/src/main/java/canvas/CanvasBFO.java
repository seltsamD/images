package canvas;

import model.xml.Project;
import org.faceless.pdf2.*;

import javax.enterprise.inject.Alternative;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Alternative
public class CanvasBFO implements Canvas {
    private PDF pdf;
    private PDFPage page;
    private PDFCanvas canvas;

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
            e.printStackTrace();
        }
    }


    @Override
    public void build(File file) {
        try {
            page.drawCanvas(canvas, 0, 0, canvas.getWidth(), canvas.getHeight());
            page.flush();
            File result = Paths.get(file.toURI()).resolve("preview.pdf").toFile();
            pdf.render(new FileOutputStream(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
