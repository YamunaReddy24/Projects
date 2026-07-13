package com.agrilink.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "expert_responses")
public class ExpertResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "query_id", nullable = false)
    private FarmerQuery farmerQuery;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expert_id", nullable = false)
    private Expert expert;

    @Column(columnDefinition = "TEXT")
    private String response;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ExpertResponse() {}

    public ExpertResponse(FarmerQuery farmerQuery, Expert expert, String response, LocalDateTime createdAt) {
        this.farmerQuery = farmerQuery;
        this.expert = expert;
        this.response = response;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FarmerQuery getFarmerQuery() {
        return farmerQuery;
    }

    public void setFarmerQuery(FarmerQuery farmerQuery) {
        this.farmerQuery = farmerQuery;
    }

    public Expert getExpert() {
        return expert;
    }

    public void setExpert(Expert expert) {
        this.expert = expert;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
