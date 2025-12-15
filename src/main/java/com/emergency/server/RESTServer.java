package com.emergency.server;

import com.emergency.service.rest.EmergencyRESTService;
import com.emergency.service.rest.ResponderRESTService;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import java.io.IOException;
import java.net.URI;

public class RESTServer {
    
    private static final String BASE_URI = "http://localhost:8081/api/";
    
    public static HttpServer startServer() {
        // Create a resource config that scans for JAX-RS resources
        final ResourceConfig rc = new ResourceConfig()
                .register(EmergencyRESTService.class)
                .register(ResponderRESTService.class)
                .register(JacksonFeature.class)
                .register(CORSFilter.class);
        
        // Create and start the server
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }
    
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("Emergency REST Service - Starting...");
        System.out.println("===========================================");
        
        try {
            final HttpServer server = startServer();
            
            System.out.println("✓ REST Service started successfully!");
            System.out.println("✓ Base URI: " + BASE_URI);
            System.out.println("===========================================");
            System.out.println("Available Endpoints:");
            System.out.println("\nEmergencies:");
            System.out.println("  GET    " + BASE_URI + "emergencies");
            System.out.println("  GET    " + BASE_URI + "emergencies/{id}");
            System.out.println("  GET    " + BASE_URI + "emergencies/status/{status}");
            System.out.println("  GET    " + BASE_URI + "emergencies/severity/{severity}");
            System.out.println("  GET    " + BASE_URI + "emergencies/count");
            System.out.println("  POST   " + BASE_URI + "emergencies");
            System.out.println("  PUT    " + BASE_URI + "emergencies/{id}");
            System.out.println("  PATCH  " + BASE_URI + "emergencies/{id}/status?status=...");
            System.out.println("  DELETE " + BASE_URI + "emergencies/{id}");
            System.out.println("\nResponders:");
            System.out.println("  GET    " + BASE_URI + "responders");
            System.out.println("  GET    " + BASE_URI + "responders/{id}");
            System.out.println("  GET    " + BASE_URI + "responders/available");
            System.out.println("  GET    " + BASE_URI + "responders/specialty/{specialty}");
            System.out.println("  POST   " + BASE_URI + "responders");
            System.out.println("  PUT    " + BASE_URI + "responders/{id}");
            System.out.println("  DELETE " + BASE_URI + "responders/{id}");
            System.out.println("===========================================");
            System.out.println("Press Ctrl+C to stop the server");
            System.out.println("===========================================");
            
            // Keep server running
            Thread.currentThread().join();
            
        } catch (IOException | InterruptedException e) {
            System.err.println("✗ Error starting REST server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    // CORS Filter to allow cross-origin requests
    @jakarta.ws.rs.ext.Provider
    public static class CORSFilter implements jakarta.ws.rs.container.ContainerResponseFilter {
        @Override
        public void filter(jakarta.ws.rs.container.ContainerRequestContext requestContext,
                          jakarta.ws.rs.container.ContainerResponseContext responseContext) {
            responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
            responseContext.getHeaders().add("Access-Control-Allow-Headers", 
                "origin, content-type, accept, authorization");
            responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders().add("Access-Control-Allow-Methods", 
                "GET, POST, PUT, DELETE, OPTIONS, HEAD, PATCH");
        }
    }
}