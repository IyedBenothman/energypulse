import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerTest{

    @Test
    void ShouldCalculateConsumption(){
        
        Customer customer = new Customer("C001","Iyed");

        customer.addReading(new MeterReading ("METER001",5.2));
        customer.addReading(new MeterReading ("METER001",12.8));

        assertEquals(18.0, customer.getTotalConsumption());
    }

    @Test
    void ShouldCountHighConsumptionReading(){

        Customer customer = new Customer("C001","Iyed");

        customer.addReading(new MeterReading("METER001", 5.1));
        customer.addReading(new MeterReading("METER001", 12.5));
        customer.addReading(new MeterReading("METER001", 11.0));

        assertEquals(2, customer.getHighConsumptionCount());
    }
}