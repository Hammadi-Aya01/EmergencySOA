package com.emergency.server;

import com.emergency.service.soap.EmergencySOAPServiceImpl;
import jakarta.xml.ws.Endpoint;

public class SOAPServer {
    
    private static final String SOAP_URL = "http://localhost:8080/emergency-soap";
    
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("Emergency SOAP Service - Starting...");
        System.out.println("===========================================");
        
        try {
            // Create and publish SOAP service
            EmergencySOAPServiceImpl service = new EmergencySOAPServiceImpl();
            Endpoint endpoint = Endpoint.publish(SOAP_URL, service);
            
            System.out.println("✓ SOAP Service started successfully!");
            System.out.println("✓ WSDL available at: " + SOAP_URL + "?wsdl");
            System.out.println("✓ Service endpoint: " + SOAP_URL);
            System.out.println("===========================================");
            System.out.println("Available Operations:");
            System.out.println("  - createEmergency");
            System.out.println("  - getEmergencyById");
            System.out.println("  - getAllEmergencies");
            System.out.println("  - getEmergenciesByStatus");
            System.out.println("  - getEmergenciesBySeverity");
            System.out.println("  - updateEmergencyStatus");
            System.out.println("  - deleteEmergency");
            System.out.println("  - getEmergencyStatistics");
            System.out.println("===========================================");
            System.out.println("Press Ctrl+C to stop the server");
            System.out.println("===========================================");
            
            // Keep server running
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("✗ Error starting SOAP server: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}