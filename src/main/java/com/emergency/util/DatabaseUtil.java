package com.emergency.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DatabaseUtil {
    
    private static EntityManagerFactory emf;
    private static final String PERSISTENCE_UNIT = "EmergencyPU";
    
    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            try {
                emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                System.out.println("✓ Database connection established successfully");
            } catch (Exception e) {
                System.err.println("✗ Error creating EntityManagerFactory: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Failed to create EntityManagerFactory", e);
            }
        }
        return emf;
    }
    
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }
    
    public static void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("Database connection closed");
        }
    }
    
    public static boolean testConnection() {
        try {
            EntityManager em = getEntityManager();
            em.createQuery("SELECT 1").getResultList();
            em.close();
            return true;
        } catch (Exception e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    public static void printDatabaseInfo() {
        System.out.println("===========================================");
        System.out.println("Database Information");
        System.out.println("===========================================");
        System.out.println("Persistence Unit: " + PERSISTENCE_UNIT);
        System.out.println("Connection Status: " + (testConnection() ? "Connected" : "Disconnected"));
        System.out.println("===========================================");
    }
}