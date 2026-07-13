package com.agrilink.model;

import jakarta.persistence.*;

@Entity
@Table(name = "experts", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Expert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;
    private String phone;
    private String specialization; // e.g., Crop Science, Soil Management, etc.
    private String experience;

    public Expert() {}

    public Expert(String name, String email, String password, String phone, String specialization, String experience) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.specialization = specialization;
        this.experience = experience;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
