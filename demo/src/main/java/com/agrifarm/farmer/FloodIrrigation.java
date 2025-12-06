package com.agrifarm.farmer;

import com.agrifarm.model.Plant;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FloodIrrigation implements IrrigationStrategy {

    private static final Logger logger = Logger.getLogger(FloodIrrigation.class.getName());

    @Override
    public double irrigate(Plant plant) {
        int growthBonus = 20;
        plant.grow(growthBonus);

        logger.info(">> [Flood] Membuka pintu air irigasi...");
        // Fix: Use formatting
        logger.log(Level.INFO, ">> [Flood] Menggenangi lahan (Metode Leb). Cocok untuk {0}", plant.getName());

        return 50.0;
    }
}