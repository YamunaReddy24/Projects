package com.agrilink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.agrilink.model.FarmerQuery;
import com.agrilink.model.Farmer;
import java.util.List;

public interface FarmerQueryRepository extends JpaRepository<FarmerQuery, Long> {
    List<FarmerQuery> findByFarmerOrderByCreatedAtDesc(Farmer farmer);
    List<FarmerQuery> findByStatusOrderByCreatedAtDesc(String status);
    List<FarmerQuery> findAllByOrderByCreatedAtDesc();
}
