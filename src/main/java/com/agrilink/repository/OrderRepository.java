package com.agrilink.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.agrilink.model.Order;
import com.agrilink.model.Buyer;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByBuyer(Buyer buyer);
    List<Order> findByCropFarmerId(Long farmerId);
}

