package com.agrilink.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @GetMapping
    public Map<String, Object> getWeather(HttpSession session) {
        // Simple hardcoded weather response that will definitely work
        Map<String, Object> weatherData = new HashMap<>();
        weatherData.put("cityName", "Bengaluru");
        weatherData.put("name", "Bengaluru");
        
        // Main weather data
        Map<String, Object> main = new HashMap<>();
        main.put("temp", 28.5);
        main.put("temperature", 28.5);
        main.put("humidity", 65);
        weatherData.put("main", main);
        
        // Weather condition
        Map<String, Object> weatherCondition = new HashMap<>();
        weatherCondition.put("main", "Clear");
        weatherCondition.put("description", "Clear Sky");
        weatherCondition.put("icon", "01d");
        weatherData.put("weather", new Object[]{weatherCondition});
        
        // Wind data
        Map<String, Object> wind = new HashMap<>();
        wind.put("speed", 3.5);
        weatherData.put("wind", wind);
        
        return weatherData;
    }
}
