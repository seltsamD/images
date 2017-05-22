package controller;

import model.db.User;
import service.UserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@Path("/user")
public class UserRest {
    @Inject
    private UserService userService;

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public User add(User user) {
        return userService.save(user);
    }

    @GET
    @Path("/full")
    @Produces("application/json")
    public List<User> getAll() {
        return userService.findAll();
    }
}
