package util;

import org.jboss.logging.Logger;
import repository.ProjectRepository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class ProjectUtils {
    private static final Logger LOGGER = Logger.getLogger(ProjectRepository.class);
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
}
