package com.agrilink.config;

import com.agrilink.model.MarketPrice;
import com.agrilink.repository.MarketPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MarketPriceRepository marketPriceRepository;

    @Override
    public void run(String... args) throws Exception {
        if (marketPriceRepository.count() == 0) {
            String[] crops = {"Rice", "Wheat", "Corn", "Tomato", "Potato"};
            double[][] prices = {
                    {3200, 3250, 3300, 3280, 3350}, // Rice
                    {2800, 2850, 2900, 2870, 2920}, // Wheat
                    {1800, 1850, 1900, 1880, 1920}, // Corn
                    {2500, 2600, 2550, 2700, 2650}, // Tomato
                    {1200, 1250, 1300, 1280, 1320}  // Potato
            };

            for (int i = 0; i < crops.length; i++) {
                for (int j = 0; j < 5; j++) {
                    MarketPrice mp = new MarketPrice();
                    mp.setCropName(crops[i]);
                    mp.setMarketPrice(prices[i][j]);
                    mp.setPriceDate(LocalDate.now().minusDays(4 - j));
                    marketPriceRepository.save(mp);
                }
            }
        }
    }
}
