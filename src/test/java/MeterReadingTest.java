import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MeterReadingTest {

    @Test
    void shouldDetectHighConsumption() {
        MeterReading reading = new MeterReading("METER-001", 15.4);

        assertTrue(reading.isHighConsumption());
    }
    @Test
    void shouldNotDetectLowConsumptionAsHigh() {
        MeterReading reading = new MeterReading("METER-002", 5.0);

        assertFalse(reading.isHighConsumption());
    }
    @Test
    void shouldNotDetectThresholdAsHighConsumption() {
    MeterReading reading = new MeterReading("METER-003", 10.0);

    assertFalse(reading.isHighConsumption());
    }
}