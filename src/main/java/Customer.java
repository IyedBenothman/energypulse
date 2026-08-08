import java.util.ArrayList;
import java.util.List;

public class Customer{

    private String customerId;
    private String name;
    private List<MeterReading> readings;

    public Customer(String customerId, String name){
        this.customerId = customerId;
        this.name = name;
        this.readings = new ArrayList<>();
    }

    public String getCustomerId(){
        return customerId;
    }

    public String getName(){
        return name;
    } 
    public void addReading(MeterReading reading){
        readings.add(reading);
    }
    public double getTotalConsumption(){
        double total = 0.0;
        for (MeterReading reading : readings){
            total += reading.getConsumptionKwh();
        }
        return total;
    }
    public int getHighConsumptionCount(){
        int total = 0;
        for(MeterReading reading : readings){
            if(reading.isHighConsumption()) {
                total++;
            }
        }
        return total;
    }
}