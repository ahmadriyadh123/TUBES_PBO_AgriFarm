import static org.junit.Assert.*;
import org.junit.Test;

public class FloodIrrigationTest {

    @Test
    public void testFloodIrrigation() {
        Plant padi = new Plant("Padi", "Dewasa");
        FloodIrrigation flood = new FloodIrrigation();

        int initialProgress = padi.getProgress();
        double waterUsed = flood.irrigate(padi);

        assertEquals("Air untuk flood harus 50 liter", 50.0, waterUsed, 0.01);
        assertTrue("Progress tanaman harus bertambah", padi.getProgress() > initialProgress);
    }
}
