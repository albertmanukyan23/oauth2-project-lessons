package com.appsdeveloperblog.keycloak.myremoteuserstorageprovider;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
public interface UsersApiService {

    @GET
    @Path("/{username}")
    User getUserDetails(@PathParam("username") String username);

    @POST
    @Path("/{username}/verify-password")
    @Produces(MediaType.APPLICATION_JSON)
    VerifyPasswordResponse verifyUserPassword(@PathParam("username") String username, String password);


}
