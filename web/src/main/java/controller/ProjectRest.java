package controller;

import model.Project;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import service.ProjectService;
import service.UserService;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;

@Path("/project")
public class ProjectRest {
    @Inject
    private UserService userService;

    @Inject
    private ProjectService projectService;

    @Context
    private SecurityContext context;

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
    @Produces("image/png")
    public Response call(@QueryParam("username") String username, @QueryParam("projectname") String projectName) {
        InputStream inputStream = null;
        //TODO: projectService should have another method for hide all of this encoding, getPreviewBody or something like that
        /*byte [] previewBody =projectService.getPreviewBody(username, projectName);
        if(previewBody==null)
            return Response.noContent().build();
        return Response.ok(previewBody).build();*/

        //Or you can wrap to static util method response of projectService.callPreview()
        /*byte [] previewBody = readBody(projectService.callPreview(username, projectName));
        if(previewBody==null)
            return Response.noContent().build();
        return Response.ok(previewBody).build();*/

        File file = projectService.callPreview(username, projectName);
        if (file == null)
            return Response.noContent().build();

        byte[] imageData = null;
        try {
            BufferedImage image = ImageIO.read(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            imageData = Base64.getEncoder().encode(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.ok(imageData).build();


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

    //TODO: move to some util class
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
