package com.emergency.service.rest;

import com.emergency.dao.ResponderDAO;
import com.emergency.model.Responder;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/responders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ResponderRESTService {
    
    private final ResponderDAO responderDAO = new ResponderDAO();
    
    @GET
    public Response getAllResponders() {
        try {
            List<Responder> responders = responderDAO.findAll();
            return Response.ok(responders).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/{id}")
    public Response getResponderById(@PathParam("id") Long id) {
        try {
            Responder responder = responderDAO.findById(id);
            if (responder == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Responder not found").build();
            }
            return Response.ok(responder).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/available")
    public Response getAvailableResponders() {
        try {
            List<Responder> responders = responderDAO.findAvailable();
            return Response.ok(responders).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/specialty/{specialty}")
    public Response getRespondersBySpecialty(@PathParam("specialty") String specialty) {
        try {
            List<Responder> responders = responderDAO.findBySpecialty(specialty);
            return Response.ok(responders).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
    
    @POST
    public Response createResponder(Responder responder) {
        try {
            Responder created = responderDAO.create(responder);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response updateResponder(@PathParam("id") Long id, Responder responder) {
        try {
            Responder existing = responderDAO.findById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Responder not found").build();
            }
            
            responder.setId(id);
            Responder updated = responderDAO.update(responder);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteResponder(@PathParam("id") Long id) {
        try {
            boolean deleted = responderDAO.delete(id);
            if (!deleted) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Responder not found").build();
            }
            return Response.ok().entity("Responder deleted successfully").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
}