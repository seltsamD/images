package canvas;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import static java.awt.Color.BLACK;

public class Canvas2D implements Canvas {
    //TODO: make it private
    Graphics2D graphics;
    BufferedImage image;

    public Canvas2D(BufferedImage bufferedImage) {
        this.image = bufferedImage;
        this.graphics = image.createGraphics();
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
            e.printStackTrace();
        }
    }

    @Override
    public InputStream build() {
        ByteArrayInputStream  result = null;
        try (ByteArrayOutputStream baos  = new ByteArrayOutputStream()){
            ImageIO.write(image, "png", baos);
            result =  new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
