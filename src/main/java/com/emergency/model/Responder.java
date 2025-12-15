package com.emergency.model;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "responders")
@XmlRootElement(name = "responder")
@XmlAccessorType(XmlAccessType.FIELD)
public class Responder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String specialty;
    
    @Column(nullable = false)
    private String phone;
    
    @Column(nullable = false)
    private String status;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public Responder() {
        this.status = "AVAILABLE";
        this.createdAt = LocalDateTime.now();
    }
    
    public Responder(String name, String specialty, String phone) {
        this();
        this.name = name;
        this.specialty = specialty;
        this.phone = phone;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return String.format("Responder[id=%d, name=%s, specialty=%s, status=%s]",
                id, name, specialty, status);
    }
}