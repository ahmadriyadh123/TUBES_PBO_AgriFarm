package com.agrifarm;

import static org.junit.Assert.*;
import org.junit.Test;

public class DripIrrigationTest {

    @Test
    public void testDripIrrigationForBibit() {
        Plant bibit = new Plant("Cabai", "Bibit");
        DripIrrigation drip = new DripIrrigation();

        double waterUsed = drip.irrigate(bibit);

        assertEquals("Air untuk bibit harus 0.5 liter", 0.5, waterUsed, 0.01);
    }
}
