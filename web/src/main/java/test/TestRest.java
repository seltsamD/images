package test;

import dao.ConfigDAO;
import model.Config;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("/rest")
public class TestRest {

    @EJB
    private ConfigDAO configDAO;

    @GET
    @Path("/full")
    @Produces("application/json")
    public List<Config> getAll() {
        return configDAO.findAll();
    }

    @GET
    @Path("/add")
    public void add(
            @QueryParam("key") String key,
            @QueryParam("value") String value
    ) {
        configDAO.save(new Config(key, value));
    }

    @GET
    @Path("/get")
    @Produces("application/json")
    public Config getByKey(
            @QueryParam("key") String key
    ) {
        return configDAO.getByKey(key);
    }

}
