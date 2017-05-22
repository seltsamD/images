package util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class ProjectUtils {

    public static <T> T getObject(File file, Class c) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(c);
        Unmarshaller unmarshaller = context.createUnmarshaller();
         return (T) unmarshaller.unmarshal(file);
    }

    //TODO: make it static and rewrite it to Generics for get rid of Object,
    // for this function Object is fine argument, but its oldschool way, use generics =)
    public static void saveObject(File file, Object o) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(o.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(o, file);
    }
}
