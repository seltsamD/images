package JAXB;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

//TODO: rename it to ProjectUtils,
// its more like converting/utility functions
//TODO: move this to utils package
public class ProjectParser {

    //TODO: make it static and rewrite it to Generics for get rid of Object,
    // returning an Object could led to cast issues,try to avoid returning Objects from your functions
    public Object getObject(File file, Class c) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(c);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object object = unmarshaller.unmarshal(file);

        return object;
    }

    //TODO: make it static and rewrite it to Generics for get rid of Object,
    // for this function Object is fine argument, but its oldschool way, use generics =)
    public void saveObject(File file, Object o) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(o.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(o, file);
    }
}
