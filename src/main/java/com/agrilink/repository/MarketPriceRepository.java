package com.agrilink.repository;

import com.agrilink.model.MarketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MarketPriceRepository extends JpaRepository<MarketPrice, Long> {
    List<MarketPrice> findByCropNameOrderByPriceDateDesc(String cropName);
    List<MarketPrice> findByPriceDateOrderByCropNameAsc(LocalDate date);
    List<MarketPrice> findByCropNameOrderByPriceDateAsc(String cropName);
}
