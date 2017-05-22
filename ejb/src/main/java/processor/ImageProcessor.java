package processor;

import java.io.File;

public interface ImageProcessor {
    File scale(File source, File target, int width, int height);
}
