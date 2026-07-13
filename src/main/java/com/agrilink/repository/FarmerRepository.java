package com.agrilink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.agrilink.model.Farmer;
import java.util.List;

public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    Farmer findByEmail(String email);
    List<Farmer> findAllByEmail(String email);
}
