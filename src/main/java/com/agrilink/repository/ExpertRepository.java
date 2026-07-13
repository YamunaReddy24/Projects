package com.agrilink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.agrilink.model.Expert;
import java.util.List;

public interface ExpertRepository extends JpaRepository<Expert, Long> {
    Expert findByEmail(String email);
    List<Expert> findAllByEmail(String email);
}
