package com.emergency.client.rest;

import com.emergency.model.Emergency;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class EmergencyRESTClient {
    
    private static final String BASE_URL = "http://localhost:8081/api/emergencies";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    public EmergencyRESTClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    public List<Emergency> getAllEmergencies() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), 
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Emergency.class));
            } else {
                System.err.println("Error: " + response.statusCode() + " - " + response.body());
                return List.of();
            }
        } catch (Exception e) {
            System.err.println("Error getting emergencies: " + e.getMessage());
            return List.of();
        }
    }
    
    public Emergency getEmergencyById(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), Emergency.class);
            } else {
                System.err.println("Error: " + response.statusCode() + " - " + response.body());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error getting emergency: " + e.getMessage());
            return null;
        }
    }
    
    public Emergency createEmergency(Emergency emergency) {
        try {
            String jsonBody = objectMapper.writeValueAsString(emergency);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 201) {
                return objectMapper.readValue(response.body(), Emergency.class);
            } else {
                System.err.println("Error: " + response.statusCode() + " - " + response.body());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error creating emergency: " + e.getMessage());
            return null;
        }
    }
    
    public Emergency updateEmergency(Long id, Emergency emergency) {
        try {
            String jsonBody = objectMapper.writeValueAsString(emergency);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), Emergency.class);
            } else {
                System.err.println("Error: " + response.statusCode() + " - " + response.body());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error updating emergency: " + e.getMessage());
            return null;
        }
    }
    
    public boolean deleteEmergency(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/" + id))
                    .DELETE()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Error deleting emergency: " + e.getMessage());
            return false;
        }
    }
    
    public List<Emergency> getEmergenciesByStatus(String status) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/status/" + status))
                    .GET()
                    .build();
            
            HttpResponse<String> response = httpClient.send(request, 
                    HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), 
                        objectMapper.getTypeFactory().constructCollectionType(List.class, Emergency.class));
            } else {
                System.err.println("Error: " + response.statusCode() + " - " + response.body());
                return List.of();
            }
        } catch (Exception e) {
            System.err.println("Error getting emergencies by status: " + e.getMessage());
            return List.of();
        }
    }
}