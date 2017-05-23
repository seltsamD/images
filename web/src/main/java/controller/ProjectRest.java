package controller;

import model.db.Project;
import org.apache.commons.io.FilenameUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import service.ProjectService;
import service.UserService;
import util.FileUtil;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
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

    private static final Logger LOGGER = Logger.getLogger(ProjectRest.class);

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
            //TODO: remove if and on project name clash just replace old one,
            // replace is more obvious
            if(projectService.isUniqueName(FilenameUtils.getBaseName(projectName))){
                projectService.save(userId, projectName, istream);
            }
            else return Response.noContent().build();

            //TODO: remove try{}catch(){} blocks, let rest method throw exceptions
            // use ExceptionMappers for handle errors on rest crash its common approach
        } catch (IOException e) {
            LOGGER.error("Error at process of upload project " + e.getMessage());
        }

        URI url = null;
        try {
            url = new URI("../");
            //TODO: remove try{}catch(){} blocks, let rest method throw exceptions
            // use ExceptionMappers for handle errors on rest crash its common approach
        } catch (URISyntaxException e) {
            LOGGER.error("Error at process of crate URI " + e.getMessage());
        }
        return Response.seeOther(url).build();
    }

}
