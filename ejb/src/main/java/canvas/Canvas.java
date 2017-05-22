package canvas;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.InputStream;

public interface Canvas {

    void drawText(int x, int y, String value);

    void drawImage(int x, int y, File file);

    InputStream build();

}
