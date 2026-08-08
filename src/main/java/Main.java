public class Main{
    public static void main(String[] args){
        MeterReading reading = new MeterReading("METER-001", 15.4);
        System.out.println(reading.getMeterId());
        System.out.println(reading.getConsumptionKwh());
        System.out.println(reading.isHighConsumption());

        Customer customer = new Customer("C001", "Iyed");

        customer.addReading(new MeterReading("METER-001",5.2));
        customer.addReading(new MeterReading("METER-001",12.8));

        System.out.println(customer.getCustomerId());
        System.out.println(customer.getName());
        System.out.println(customer.getTotalConsumption());

        System.out.println(customer.getHighConsumptionCount());
    }
}