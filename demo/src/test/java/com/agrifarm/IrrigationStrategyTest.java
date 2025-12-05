package com.agrifarm;

import com.agrifarm.farmer.DripIrrigation;
import com.agrifarm.farmer.FloodIrrigation;
import com.agrifarm.farmer.SprinklerIrrigation;
import com.agrifarm.model.Plant;
import org.junit.Test;
import static org.junit.Assert.*;

public class IrrigationStrategyTest {

    // Test 1: Menguji Logika Flood Irrigation (Untuk Padi)
    @Test
    public void testFloodIrrigationLogic() {
        Plant padi = new Plant("Padi", "Dewasa");
        FloodIrrigation flood = new FloodIrrigation();

        int initialProgress = padi.getProgress();
        double waterUsed = flood.irrigate(padi);

        assertEquals("Air untuk flood harus 50.0 liter", 50.0, waterUsed, 0.01);
        assertTrue("Progress tanaman harus bertambah", padi.getProgress() > initialProgress);
    }

    // Test 2: Menguji Logika Sprinkler Irrigation
    @Test
    public void testSprinklerIrrigationLogic() {
        Plant jagung = new Plant("Jagung", "Dewasa");
        SprinklerIrrigation sprinkler = new SprinklerIrrigation();

        double waterUsed = sprinkler.irrigate(jagung);

        // Sprinkler di set return 5.0
        assertEquals(5.0, waterUsed, 0.01);
    }

    // Test 3: Menguji Logika Drip Irrigation pada Bibit
    @Test
    public void testDripIrrigationForBibit() {
        Plant bibit = new Plant("Cabai", "Bibit");
        DripIrrigation drip = new DripIrrigation();

        double waterUsed = drip.irrigate(bibit);

        assertEquals("Air untuk bibit harus 0.5 liter", 0.5, waterUsed, 0.01);
    }
}