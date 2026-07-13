package com.agrilink.service;

import com.agrilink.model.MarketPrice;
import com.agrilink.repository.MarketPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MarketPriceService {

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    public List<MarketPrice> getAllMarketPrices() {
        return marketPriceRepository.findAll();
    }

    public List<MarketPrice> getCurrentMarketPrices() {
        return marketPriceRepository.findByPriceDateOrderByCropNameAsc(LocalDate.now());
    }

    public List<MarketPrice> getPricesByCrop(String cropName) {
        return marketPriceRepository.findByCropNameOrderByPriceDateAsc(cropName);
    }

    public MarketPrice saveMarketPrice(MarketPrice marketPrice) {
        return marketPriceRepository.save(marketPrice);
    }

    public List<MarketPrice> getTopCropsByPrice() {
        return marketPriceRepository.findByPriceDateOrderByCropNameAsc(LocalDate.now());
    }
}
