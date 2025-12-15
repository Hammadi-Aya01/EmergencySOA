package com.emergency.service.soap;

import com.emergency.dao.EmergencyDAO;
import com.emergency.model.*;
import jakarta.jws.WebService;
import java.util.List;

@WebService(endpointInterface = "com.emergency.service.soap.EmergencySOAPService")
public class EmergencySOAPServiceImpl implements EmergencySOAPService {
    
    private final EmergencyDAO emergencyDAO;
    
    public EmergencySOAPServiceImpl() {
        this.emergencyDAO = new EmergencyDAO();
    }
    
    @Override
    public Emergency createEmergency(String type, String description, String location,
                                    String severity, String reporterName, String reporterPhone) {
        try {
            Emergency emergency = new Emergency();
            emergency.setType(EmergencyType.valueOf(type.toUpperCase()));
            emergency.setDescription(description);
            emergency.setLocation(location);
            emergency.setSeverity(Severity.valueOf(severity.toUpperCase()));
            emergency.setReporterName(reporterName);
            emergency.setReporterPhone(reporterPhone);
            emergency.setStatus(Status.PENDING);
            
            return emergencyDAO.create(emergency);
        } catch (Exception e) {
            System.err.println("Error creating emergency: " + e.getMessage());
            throw new RuntimeException("Failed to create emergency: " + e.getMessage());
        }
    }
    
    @Override
    public Emergency getEmergencyById(Long id) {
        try {
            Emergency emergency = emergencyDAO.findById(id);
            if (emergency == null) {
                throw new RuntimeException("Emergency not found with id: " + id);
            }
            return emergency;
        } catch (Exception e) {
            System.err.println("Error getting emergency: " + e.getMessage());
            throw new RuntimeException("Failed to get emergency: " + e.getMessage());
        }
    }
    
    @Override
    public List<Emergency> getAllEmergencies() {
        try {
            return emergencyDAO.findAll();
        } catch (Exception e) {
            System.err.println("Error getting all emergencies: " + e.getMessage());
            throw new RuntimeException("Failed to get emergencies: " + e.getMessage());
        }
    }
    
    @Override
    public List<Emergency> getEmergenciesByStatus(String status) {
        try {
            Status statusEnum = Status.valueOf(status.toUpperCase());
            return emergencyDAO.findByStatus(statusEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        } catch (Exception e) {
            System.err.println("Error getting emergencies by status: " + e.getMessage());
            throw new RuntimeException("Failed to get emergencies: " + e.getMessage());
        }
    }
    
    @Override
    public List<Emergency> getEmergenciesBySeverity(String severity) {
        try {
            Severity severityEnum = Severity.valueOf(severity.toUpperCase());
            return emergencyDAO.findBySeverity(severityEnum);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid severity: " + severity);
        } catch (Exception e) {
            System.err.println("Error getting emergencies by severity: " + e.getMessage());
            throw new RuntimeException("Failed to get emergencies: " + e.getMessage());
        }
    }
    
    @Override
    public Emergency updateEmergencyStatus(Long id, String status) {
        try {
            Emergency emergency = emergencyDAO.findById(id);
            if (emergency == null) {
                throw new RuntimeException("Emergency not found with id: " + id);
            }
            
            Status statusEnum = Status.valueOf(status.toUpperCase());
            emergency.setStatus(statusEnum);
            
            return emergencyDAO.update(emergency);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        } catch (Exception e) {
            System.err.println("Error updating emergency status: " + e.getMessage());
            throw new RuntimeException("Failed to update emergency: " + e.getMessage());
        }
    }
    
    @Override
    public boolean deleteEmergency(Long id) {
        try {
            return emergencyDAO.delete(id);
        } catch (Exception e) {
            System.err.println("Error deleting emergency: " + e.getMessage());
            throw new RuntimeException("Failed to delete emergency: " + e.getMessage());
        }
    }
    
    @Override
    public String getEmergencyStatistics() {
        try {
            List<Emergency> all = emergencyDAO.findAll();
            long total = all.size();
            long pending = all.stream().filter(e -> e.getStatus() == Status.PENDING).count();
            long inProgress = all.stream().filter(e -> e.getStatus() == Status.IN_PROGRESS).count();
            long resolved = all.stream().filter(e -> e.getStatus() == Status.RESOLVED).count();
            long critical = all.stream().filter(e -> e.getSeverity() == Severity.CRITICAL).count();
            
            return String.format(
                "Total: %d | Pending: %d | In Progress: %d | Resolved: %d | Critical: %d",
                total, pending, inProgress, resolved, critical
            );
        } catch (Exception e) {
            System.err.println("Error getting statistics: " + e.getMessage());
            throw new RuntimeException("Failed to get statistics: " + e.getMessage());
        }
    }
}