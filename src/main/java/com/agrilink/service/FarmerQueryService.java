package com.agrilink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.agrilink.model.FarmerQuery;
import com.agrilink.model.Farmer;
import com.agrilink.repository.FarmerQueryRepository;
import com.agrilink.repository.FarmerRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FarmerQueryService {

    @Autowired
    private FarmerQueryRepository farmerQueryRepository;
    
    @Autowired
    private FarmerRepository farmerRepository;

    public FarmerQuery createQuery(Farmer farmer, String subject, String question) {
        // Get a fresh Farmer from DB
        Farmer freshFarmer = farmerRepository.findById(farmer.getId()).orElse(null);
        if (freshFarmer == null) {
            return null;
        }
        
        FarmerQuery query = new FarmerQuery();
        query.setFarmer(freshFarmer);
        query.setSubject(subject);
        query.setQuestion(question);
        query.setStatus("PENDING");
        query.setCreatedAt(LocalDateTime.now());
        return farmerQueryRepository.save(query);
    }

    public List<FarmerQuery> getQueriesByFarmer(Farmer farmer) {
        return farmerQueryRepository.findByFarmerOrderByCreatedAtDesc(farmer);
    }

    public List<FarmerQuery> getAllQueries() {
        return farmerQueryRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<FarmerQuery> getQueriesByStatus(String status) {
        return farmerQueryRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    public FarmerQuery getQueryById(Long id) {
        return farmerQueryRepository.findById(id).orElse(null);
    }

    public FarmerQuery updateQueryStatus(Long id, String status) {
        FarmerQuery query = getQueryById(id);
        if (query != null) {
            query.setStatus(status);
            return farmerQueryRepository.save(query);
        }
        return null;
    }
}
