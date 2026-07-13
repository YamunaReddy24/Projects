package com.agrilink.controller;

import com.agrilink.model.Crop;
import com.agrilink.model.MarketPrice;
import com.agrilink.repository.CropRepository;
import com.agrilink.service.MarketPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/market")
public class MarketPriceController {

    @Autowired
    private MarketPriceService marketPriceService;

    @Autowired
    private CropRepository cropRepository;

    @GetMapping("/dashboard")
    public String marketDashboard(Model model) {
        LocalDate today = LocalDate.now();
        List<MarketPrice> currentPrices = marketPriceService.getCurrentMarketPrices();
        if (currentPrices.isEmpty()) {
            currentPrices = marketPriceService.getAllMarketPrices().stream()
                .collect(Collectors.groupingBy(MarketPrice::getCropName))
                .values().stream()
                .map(list -> list.get(list.size() - 1))
                .collect(Collectors.toList());
        }

        List<MarketPrice> topCrops = currentPrices.stream()
                .sorted(Comparator.comparingDouble(MarketPrice::getMarketPrice).reversed())
                .limit(5)
                .collect(Collectors.toList());

        List<String> cropNames = Arrays.asList("Rice", "Wheat", "Corn", "Tomato", "Potato");
        Map<String, List<Double>> priceTrends = new HashMap<>();
        for (String crop : cropNames) {
            List<MarketPrice> prices = marketPriceService.getPricesByCrop(crop);
            priceTrends.put(crop, prices.stream().map(MarketPrice::getMarketPrice).collect(Collectors.toList()));
        }

        model.addAttribute("currentPrices", currentPrices);
        model.addAttribute("topCrops", topCrops);
        model.addAttribute("priceTrends", priceTrends);

        return "market_dashboard";
    }

    @GetMapping("/comparison")
    public String priceComparison(Model model) {
        List<Crop> farmerCrops = cropRepository.findAll();
        LocalDate today = LocalDate.now();
        List<MarketPrice> marketPrices = marketPriceService.getCurrentMarketPrices();
        
        if (marketPrices.isEmpty()) {
            marketPrices = marketPriceService.getAllMarketPrices().stream()
                .collect(Collectors.groupingBy(MarketPrice::getCropName))
                .values().stream()
                .map(list -> list.get(list.size() - 1))
                .collect(Collectors.toList());
        }

        Map<String, Double> marketPriceMap = marketPrices.stream()
                .collect(Collectors.toMap(MarketPrice::getCropName, MarketPrice::getMarketPrice, (a, b) -> a));

        List<Map<String, Object>> comparisonList = new ArrayList<>();
        for (Crop crop : farmerCrops) {
            Map<String, Object> item = new HashMap<>();
            item.put("cropName", crop.getCropName());
            item.put("farmerPrice", crop.getPrice());
            double mPrice = marketPriceMap.getOrDefault(crop.getCropName(), 0.0);
            item.put("marketPrice", mPrice);
            item.put("difference", mPrice - crop.getPrice());
            comparisonList.add(item);
        }

        model.addAttribute("comparisonList", comparisonList);
        return "price_comparison";
    }
}
