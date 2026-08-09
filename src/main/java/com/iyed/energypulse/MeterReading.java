package com.iyed.energypulse;
public class MeterReading{
    private String MeterId;
    private double consumptionKwh;
    public MeterReading(String MeterId, double consumptionKwh){
        this.MeterId = MeterId;
        this.consumptionKwh = consumptionKwh;
    }
    public String getMeterId(){
        return MeterId;
    }
    public double getConsumptionKwh(){
        return consumptionKwh;  
    }
    public boolean isHighConsumption(){
        return consumptionKwh > 10;
    }
}