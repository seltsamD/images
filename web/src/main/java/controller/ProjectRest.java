package controller;

import model.Project;
import org.apache.commons.io.FilenameUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import service.ProjectService;
import service.UserService;
import util.FileUtil;

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
        byte[] previewBody = projectService.getPreviewBody(username, projectName);
        if (previewBody == null)
            return Response.noContent().build();
        return Response.ok(previewBody).build();
    }

    @POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    @Produces("application/json")
    public Response uploadFile(MultipartFormDataInput input) {
        long userId = userService.getIdByUsername(context.getUserPrincipal().getName());
        String projectName = null;

        InputPart inPart = input.getFormDataMap().get("file").get(0);
        try (InputStream istream = inPart.getBody(InputStream.class, null)){
            projectName = FileUtil.getFileName(inPart.getHeaders());
            if(projectService.isUniqueName(FilenameUtils.getBaseName(projectName))){
                projectService.save(userId, projectName, istream);
            }
            else return Response.noContent().build();

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

}
