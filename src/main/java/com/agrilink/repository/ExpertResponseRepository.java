package com.agrilink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.agrilink.model.ExpertResponse;
import com.agrilink.model.FarmerQuery;
import java.util.List;

public interface ExpertResponseRepository extends JpaRepository<ExpertResponse, Long> {
    List<ExpertResponse> findByFarmerQueryOrderByCreatedAtDesc(FarmerQuery farmerQuery);
    List<ExpertResponse> findByExpertIdOrderByCreatedAtDesc(Long expertId);
}
