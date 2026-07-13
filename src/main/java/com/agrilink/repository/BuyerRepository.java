package com.agrilink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agrilink.model.Buyer;

@Repository
public interface BuyerRepository extends JpaRepository<Buyer, Long> {

    Buyer findByEmail(String email);

    // ✅ Add this for login
    Buyer findByEmailAndPassword(String email, String password);
}
