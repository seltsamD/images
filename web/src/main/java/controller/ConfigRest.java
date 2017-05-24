package controller;

import model.db.Config;
import service.ConfigService;
import service.ProjectService;
import service.UserService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/config")
public class ConfigRest {

    @Inject
    private ConfigService configService;

    @GET
    @Path("/full")
    @Produces("application/json")
    public List<Config> findAll() {
        return configService.findAll();
    }

}
