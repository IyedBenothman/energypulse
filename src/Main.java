public class Main{
    public static void main(String[] args){
        MeterReading reading = new MeterReading("METER-001", 15.4);
        System.out.println(reading.getMeterId());
        System.out.println(reading.getconsumptionKwh());
        System.out.println(reading.isHighConsumtion());
    }
}