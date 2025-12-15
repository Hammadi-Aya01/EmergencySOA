package com.emergency.service.soap;

import com.emergency.model.Emergency;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.util.List;

@WebService
public interface EmergencySOAPService {
    
    @WebMethod
    Emergency createEmergency(
        @WebParam(name = "type") String type,
        @WebParam(name = "description") String description,
        @WebParam(name = "location") String location,
        @WebParam(name = "severity") String severity,
        @WebParam(name = "reporterName") String reporterName,
        @WebParam(name = "reporterPhone") String reporterPhone
    );
    
    @WebMethod
    Emergency getEmergencyById(@WebParam(name = "id") Long id);
    
    @WebMethod
    List<Emergency> getAllEmergencies();
    
    @WebMethod
    List<Emergency> getEmergenciesByStatus(@WebParam(name = "status") String status);
    
    @WebMethod
    List<Emergency> getEmergenciesBySeverity(@WebParam(name = "severity") String severity);
    
    @WebMethod
    Emergency updateEmergencyStatus(
        @WebParam(name = "id") Long id,
        @WebParam(name = "status") String status
    );
    
    @WebMethod
    boolean deleteEmergency(@WebParam(name = "id") Long id);
    
    @WebMethod
    String getEmergencyStatistics();
}