package com.emergency.service.rest;

import com.emergency.dao.EmergencyDAO;
import com.emergency.model.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/emergencies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmergencyRESTService {
    
    private final EmergencyDAO emergencyDAO = new EmergencyDAO();
    
    @GET
    public Response getAllEmergencies() {
        try {
            List<Emergency> emergencies = emergencyDAO.findAll();
            return Response.ok(emergencies).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/{id}")
    public Response getEmergencyById(@PathParam("id") Long id) {
        try {
            Emergency emergency = emergencyDAO.findById(id);
            if (emergency == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Emergency not found").build();
            }
            return Response.ok(emergency).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/status/{status}")
    public Response getEmergenciesByStatus(@PathParam("status") String status) {
        try {
            Status statusEnum = Status.valueOf(status.toUpperCase());
            List<Emergency> emergencies = emergencyDAO.findByStatus(statusEnum);
            return Response.ok(emergencies).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid status: " + status).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/severity/{severity}")
    public Response getEmergenciesBySeverity(@PathParam("severity") String severity) {
        try {
            Severity severityEnum = Severity.valueOf(severity.toUpperCase());
            List<Emergency> emergencies = emergencyDAO.findBySeverity(severityEnum);
            return Response.ok(emergencies).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid severity: " + severity).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
    
    @POST
    public Response createEmergency(Emergency emergency) {
        try {
            if (emergency.getStatus() == null) {
                emergency.setStatus(Status.PENDING);
            }
            Emergency created = emergencyDAO.create(emergency);
            return Response.status(Response.Status.CREATED).entity(created).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
    
    @PUT
    @Path("/{id}")
    public Response updateEmergency(@PathParam("id") Long id, Emergency emergency) {
        try {
            Emergency existing = emergencyDAO.findById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Emergency not found").build();
            }
            
            emergency.setId(id);
            Emergency updated = emergencyDAO.update(emergency);
            return Response.ok(updated).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
    
    @PATCH
    @Path("/{id}/status")
    public Response updateEmergencyStatus(@PathParam("id") Long id, 
                                         @QueryParam("status") String status) {
        try {
            Emergency emergency = emergencyDAO.findById(id);
            if (emergency == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Emergency not found").build();
            }
            
            Status statusEnum = Status.valueOf(status.toUpperCase());
            emergency.setStatus(statusEnum);
            Emergency updated = emergencyDAO.update(emergency);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid status: " + status).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteEmergency(@PathParam("id") Long id) {
        try {
            boolean deleted = emergencyDAO.delete(id);
            if (!deleted) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Emergency not found").build();
            }
            return Response.ok().entity("Emergency deleted successfully").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
    
    @GET
    @Path("/count")
    public Response getCount() {
        try {
            long count = emergencyDAO.count();
            return Response.ok().entity("{\"count\": " + count + "}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error: " + e.getMessage()).build();
        }
    }
}