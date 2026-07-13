package com.agrilink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.agrilink.model.ExpertResponse;
import com.agrilink.model.FarmerQuery;
import com.agrilink.model.Expert;
import com.agrilink.repository.ExpertResponseRepository;
import com.agrilink.repository.FarmerQueryRepository;
import com.agrilink.repository.ExpertRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpertResponseService {

    @Autowired
    private ExpertResponseRepository expertResponseRepository;
    
    @Autowired
    private FarmerQueryRepository farmerQueryRepository;
    
    @Autowired
    private ExpertRepository expertRepository;

    public ExpertResponse createResponse(FarmerQuery query, Expert expert, String response) {
        // Get fresh entities from DB
        FarmerQuery freshQuery = farmerQueryRepository.findById(query.getId()).orElse(null);
        Expert freshExpert = expertRepository.findById(expert.getId()).orElse(null);
        
        if (freshQuery == null || freshExpert == null) {
            return null;
        }
        
        ExpertResponse expertResponse = new ExpertResponse();
        expertResponse.setFarmerQuery(freshQuery);
        expertResponse.setExpert(freshExpert);
        expertResponse.setResponse(response);
        expertResponse.setCreatedAt(LocalDateTime.now());
        return expertResponseRepository.save(expertResponse);
    }

    public List<ExpertResponse> getResponsesByQuery(FarmerQuery query) {
        return expertResponseRepository.findByFarmerQueryOrderByCreatedAtDesc(query);
    }

    public List<ExpertResponse> getResponsesByExpert(Long expertId) {
        return expertResponseRepository.findByExpertIdOrderByCreatedAtDesc(expertId);
    }
}
