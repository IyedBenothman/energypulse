package com.iyed.energypulse;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

@Entity
public class Customer{

    @Id
    private String customerId;
    private String name;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<MeterReading> readings = new ArrayList<>();
    
    public List<MeterReading> getReadings(){
        return readings;
    }

    protected Customer(){
    }

    public Customer(String customerId, String name){
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId(){
        return customerId;
    }

    public String getName(){
        return name;
    } 
    public void addReading(MeterReading reading){
        readings.add(reading);
        reading.setCustomer(this);
    }
    public double getTotalConsumption(){
        return readings.stream()
            .mapToDouble(MeterReading::getConsumptionKwh)
            .sum();
    }
    public int getHighConsumptionCount(){
        return (int) readings.stream()
            .filter(MeterReading::isHighConsumption)
            .count();
    }

    public void rename(String name){
        this.name = name;
    }
}