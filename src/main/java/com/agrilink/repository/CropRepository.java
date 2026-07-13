package com.agrilink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.agrilink.model.Crop;
import java.util.List;

public interface CropRepository extends JpaRepository<Crop, Long> {
    List<Crop> findByFarmerId(Long farmerId);
}
