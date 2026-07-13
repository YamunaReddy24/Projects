package com.agrilink.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Main {
    @JsonProperty("temp")
    private double temperature;
    
    private double temp; // For compatibility

    @JsonProperty("humidity")
    private int humidity;

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { 
        this.temperature = temperature; 
        this.temp = temperature; 
    }
    
    public double getTemp() { return temp; }
    public void setTemp(double temp) { 
        this.temp = temp; 
        this.temperature = temp; 
    }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }
}
