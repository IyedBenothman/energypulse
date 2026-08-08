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
    public double getconsumptionKwh(){
        return consumptionKwh;  
    }
    public boolean isHighConsumtion(){
        return consumptionKwh > 10;
    }
}