package controller;

import model.Project;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import service.ProjectService;
import service.UserService;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Path("/project")
public class ProjectRest {
    @Inject
    private UserService userService;

    @Inject
    private ProjectService projectService;

    @Context
    private SecurityContext context;


    //TODO: replace all work with messages to EJB module, get rid of jms, ejb dependency in WEB module.
 /*   @EJB
    ProjectResourceMdb resourceMdb;*/

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/jms/queue/projectQueue")
    private Queue queue;

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

    @GET
    @Path("/call")
    @Produces("image/jpg")
    public Response call(@QueryParam("username") String username, @QueryParam("projectname") String projectName) {
        InputStream inputStream = null;
        if (projectService.callPreview(username, projectName) == null)
            return Response.status(404).build();
        try {
            inputStream = new FileInputStream(projectService.callPreview(username, projectName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Response.ok(inputStream).build();

    }

    @POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    @Produces("application/json")
    public Response uploadFile(MultipartFormDataInput input) {
        long userId = userService.getIdByUsername(context.getUserPrincipal().getName());
        String projectName = null;

        InputPart inPart = input.getFormDataMap().get("file").get(0);
        try {
            InputStream istream = inPart.getBody(InputStream.class, null);
            projectName = getFileName(inPart.getHeaders());

            projectService.save(userId, projectName, istream);

            try {
                Connection connection = connectionFactory.createConnection();
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer messageProducer = (MessageProducer) session.createProducer(queue);
                TextMessage textMessage = session.createTextMessage();
                textMessage.setStringProperty("userId", String.valueOf(userId));
                textMessage.setStringProperty("projectName", projectName);
                messageProducer.send(textMessage);
            } catch (JMSException e) {
                e.printStackTrace();
            }

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
