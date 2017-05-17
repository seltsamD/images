package controller;

import model.Project;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import service.ProjectService;
import service.UserService;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.jms.*;
@Path("/project")
//@JMSDestinationDefinitions(
//        value =  {
//                @JMSDestinationDefinition(
//                        name = "java:/queue/ProjectQueue",
//                        interfaceName = "javax.jms.Queue",
//                        destinationName = "projectMDB"
//                )
//        }
//)
public class ProjectRest {
    @Inject
    private UserService userService;

    @Inject
    private ProjectService projectService;

    @Context
    private SecurityContext context;

//    @Resource(mappedName="java:/ConnectionFactory")
//    private ConnectionFactory connectionFactory;
//
//    @Resource(mappedName = "java:/queue/ProjectQueue")
//    private Queue queue;

    @GET
    @Produces("application/json")
    public List<Project> getByUser() {
        return projectService.getByUserName(context.getUserPrincipal().getName());
    }

    @GET
    @Path("/full")
    @Produces("application/json")
    public List<Project> getAll() {
        return projectService.findAll();
    }

    @DELETE
    public void delete(@QueryParam("id") long id) {
        projectService.delete(id);
    }

    @POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input) {
        long userId = userService.getIdByUsername(context.getUserPrincipal().getName());
        InputPart inPart = input.getFormDataMap().get("file").get(0);
        try {
            InputStream istream = inPart.getBody(InputStream.class, null);
            String projectName = getFileName(inPart.getHeaders());

//
//            Connection connection = null;
//            try {
//                 Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//                MessageProducer messageProducer = (MessageProducer) session.createProducer(queue);
//                TextMessage textMessage = session.createTextMessage();
//                textMessage.setStringProperty("projectName", projectName);
//                messageProducer.send(textMessage);
//            } catch (JMSException e) {
//                e.printStackTrace();
//            }


            projectService.save(userId, projectName, istream);

        } catch (IOException e) {
            e.printStackTrace();
        }

        URI url = null;
        try {
            url = new URI("../");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Response.seeOther(url).build();
    }

    private String getFileName(MultivaluedMap<String, String> header) {

        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }
}
