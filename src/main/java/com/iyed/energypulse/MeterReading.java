package com.iyed.energypulse;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class MeterReading{

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    private String MeterId;
    private double consumptionKwh;

    protected MeterReading(){
    }
    
    public MeterReading(String MeterId, double consumptionKwh){
        this.MeterId = MeterId;
        this.consumptionKwh = consumptionKwh;
    }

    public Long getId(){
        return id;
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

    void setCustomer(Customer customer){
        this.customer = customer;
    }

}