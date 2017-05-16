package controller;

import model.Project;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import service.ProjectService;
import service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.*;
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

    @GET
    @Produces("application/json")
    public List<Project> getByUser() {
        return projectService.getByUser(userService.getIdByUsername(context.getUserPrincipal().getName()));
    }

    @GET
    @Path("/full")
    @Produces("application/json")
    public List<Object> getAll() {
        return projectService.findAllWithUser();
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
            String projectName = input.getFormDataPart("name", String.class, null);
            InputStream istream = inPart.getBody(InputStream.class, null);
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
}
