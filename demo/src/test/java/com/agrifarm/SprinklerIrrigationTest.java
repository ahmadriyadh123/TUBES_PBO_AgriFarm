import static org.junit.Assert.*;
import org.junit.Test;

public class SprinklerIrrigationTest {

    @Test
    public void testSprinklerIrrigation() {
        Plant jagung = new Plant("Jagung", "Dewasa");
        SprinklerIrrigation sprinkler = new SprinklerIrrigation();

        double waterUsed = sprinkler.irrigate(jagung);

        assertEquals("Sprinkler harus menggunakan 5 liter air", 5.0, waterUsed, 0.01);
    }
}
