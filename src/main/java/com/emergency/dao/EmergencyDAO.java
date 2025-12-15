package com.emergency.dao;

import com.emergency.model.Emergency;
import com.emergency.model.Status;
import com.emergency.model.Severity;
import jakarta.persistence.*;
import java.util.List;

public class EmergencyDAO {
    private static final String PERSISTENCE_UNIT = "EmergencyPU";
    private EntityManagerFactory emf;
    
    public EmergencyDAO() {
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        } catch (Exception e) {
            System.err.println("Error creating EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public Emergency create(Emergency emergency) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(emergency);
            em.getTransaction().commit();
            return emergency;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error creating emergency: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public Emergency findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Emergency.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<Emergency> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Emergency> query = em.createQuery(
                "SELECT e FROM Emergency e ORDER BY e.createdAt DESC", Emergency.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Emergency> findByStatus(Status status) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Emergency> query = em.createQuery(
                "SELECT e FROM Emergency e WHERE e.status = :status ORDER BY e.createdAt DESC", 
                Emergency.class);
            query.setParameter("status", status);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Emergency> findBySeverity(Severity severity) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Emergency> query = em.createQuery(
                "SELECT e FROM Emergency e WHERE e.severity = :severity ORDER BY e.createdAt DESC", 
                Emergency.class);
            query.setParameter("severity", severity);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Emergency update(Emergency emergency) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Emergency updated = em.merge(emergency);
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error updating emergency: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public boolean delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Emergency emergency = em.find(Emergency.class, id);
            if (emergency != null) {
                em.remove(emergency);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().commit();
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting emergency: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public long count() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT COUNT(e) FROM Emergency e", Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    
    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}