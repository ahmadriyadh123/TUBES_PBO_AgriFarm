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
        // Setup Objek
        Plant padi = new Plant("Padi", "Dewasa");
        FloodIrrigation flood = new FloodIrrigation();

        // Simpan progress awal
        int initialProgress = padi.getProgress();

        // Eksekusi Strategy
        double waterUsed = flood.irrigate(padi);

        // Assertions (Verifikasi)
        // 1. Cek penggunaan air (Harus 50.0 sesuai kode FloodIrrigation)
        assertEquals("Air untuk flood harus 50.0 liter", 50.0, waterUsed, 0.01);
        
        // 2. Cek pertumbuhan (Harus bertambah 20)
        // Karena Plant.grow menambah progress, kita cek apakah meningkat
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
        Plant bibit = new Plant("Cabai", "Bibit"); // Case Bibit
        DripIrrigation drip = new DripIrrigation();

        double waterUsed = drip.irrigate(bibit);

        // Drip logic: Jika bibit, baseWater = 0.5
        assertEquals("Air untuk bibit harus 0.5 liter", 0.5, waterUsed, 0.01);
    }
}