package controller;



import model.Config;
import service.ConfigService;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("/rest/config")
public class ConfigRest {

    @EJB
    private ConfigService configService;


    @GET
    @Path("/full")
    @Produces("application/json")
    public List<?> findAll() {
        return configService.findAll();
    }

    @GET
    @Path("/add")
    public void add(
            @QueryParam("key") String key,
            @QueryParam("value") String value
    ) {
       configService.create(key, value);
    }

}
