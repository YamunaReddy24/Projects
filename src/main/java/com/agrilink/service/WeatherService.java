package com.agrilink.service;

import com.agrilink.dto.*;
import com.agrilink.model.Farmer;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${openweather.api.key:demo}")
    private String apiKey;

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String DEFAULT_CITY = "Bengaluru";
    private static final String UNITS = "metric";

    public WeatherResponse getWeather(HttpSession session) {
        String city = getCityFromSession(session);
        logger.info("Fetching weather for city: {}", city);
        WeatherResponse response = fetchWeather(city);
        
        // If API fails, return mock data
        if (response == null) {
            logger.info("API failed, returning mock weather data");
            return getMockWeatherData(city);
        }
        return response;
    }

    private String getCityFromSession(HttpSession session) {
        Farmer farmer = (Farmer) session.getAttribute("farmer");
        if (farmer != null && farmer.getLocation() != null && !farmer.getLocation().isEmpty()) {
            return farmer.getLocation();
        }
        return DEFAULT_CITY;
    }

    private WeatherResponse fetchWeather(String city) {
        // If API key is still the default "demo" or "YOUR_API_KEY_HERE", don't call API
        if ("demo".equals(apiKey) || "YOUR_API_KEY_HERE".equals(apiKey)) {
            logger.info("Using mock weather data (no API key set)");
            return null;
        }
        
        try {
            String url = String.format("%s?q=%s&units=%s&appid=%s", BASE_URL, city, UNITS, apiKey);
            logger.info("Calling weather API: {}", url);
            WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
            logger.info("Weather API response: {}", response);
            return response;
        } catch (HttpClientErrorException e) {
            logger.error("HTTP error fetching weather: {} - {}", e.getStatusCode(), e.getResponseBodyAsString(), e);
            return null;
        } catch (Exception e) {
            logger.error("Error fetching weather", e);
            return null;
        }
    }

    private WeatherResponse getMockWeatherData(String city) {
        WeatherResponse mock = new WeatherResponse();
        mock.setCityName(city);
        
        // Mock main data - set both for compatibility
        Main main = new Main();
        // Use reflection to set both temp and temperature if needed, but for our DTO it's temperature
        main.setTemperature(28.5);
        main.setHumidity(65);
        mock.setMain(main);
        
        // Mock weather condition
        Weather weather = new Weather();
        weather.setMain("Clear");
        weather.setDescription("Clear Sky");
        weather.setIcon("01d");
        List<Weather> weatherList = new ArrayList<>();
        weatherList.add(weather);
        mock.setWeather(weatherList);
        
        // Mock wind data
        Wind wind = new Wind();
        wind.setSpeed(3.5); // m/s, which is ~12.6 km/h
        mock.setWind(wind);
        
        return mock;
    }
}
