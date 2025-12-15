package com.emergency.dao;

import com.emergency.model.Responder;
import jakarta.persistence.*;
import java.util.List;

public class ResponderDAO {
    private static final String PERSISTENCE_UNIT = "EmergencyPU";
    private EntityManagerFactory emf;
    
    public ResponderDAO() {
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        } catch (Exception e) {
            System.err.println("Error creating EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public Responder create(Responder responder) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(responder);
            em.getTransaction().commit();
            return responder;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error creating responder: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public Responder findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Responder.class, id);
        } finally {
            em.close();
        }
    }
    
    public List<Responder> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Responder> query = em.createQuery(
                "SELECT r FROM Responder r ORDER BY r.name", Responder.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Responder> findBySpecialty(String specialty) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Responder> query = em.createQuery(
                "SELECT r FROM Responder r WHERE r.specialty = :specialty", Responder.class);
            query.setParameter("specialty", specialty);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Responder> findAvailable() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Responder> query = em.createQuery(
                "SELECT r FROM Responder r WHERE r.status = 'AVAILABLE'", Responder.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public Responder update(Responder responder) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Responder updated = em.merge(responder);
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error updating responder: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public boolean delete(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Responder responder = em.find(Responder.class, id);
            if (responder != null) {
                em.remove(responder);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().commit();
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error deleting responder: " + e.getMessage(), e);
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