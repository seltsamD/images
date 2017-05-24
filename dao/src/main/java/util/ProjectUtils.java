package util;

import org.jboss.logging.Logger;
import repository.ProjectRepository;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ProjectUtils {

    public static enum PREVIEW_TYPES {PNG,PDF};


    private static final Logger LOGGER = Logger.getLogger(ProjectUtils.class);

    @SuppressWarnings("unchecked")
    public static <T> T getObject(File file, Class<T> c) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(c);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(file);
    }

    public static <T> void saveObject(File file, T o) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(o.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(o, file);
    }

    public static byte[] prepareImage(File file){
        byte[] imageData = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(file);
            ImageIO.write(image, "png", baos);
            imageData = Base64.getEncoder().encode(baos.toByteArray());
        } catch (IOException e) {
            LOGGER.error("Error at process get preview body " + e.getMessage());
        }

        return imageData;
    }
}
