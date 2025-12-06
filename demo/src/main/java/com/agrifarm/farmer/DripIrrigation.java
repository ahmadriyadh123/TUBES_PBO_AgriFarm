package com.agrifarm.farmer;

import com.agrifarm.model.Plant;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DripIrrigation implements IrrigationStrategy {

    private static final Logger logger = Logger.getLogger(DripIrrigation.class.getName());

    @Override
    public double irrigate(Plant plant) {
        int growthBonus = 25;
        plant.grow(growthBonus);

        logger.info(">> [Drip] Menyalakan pompa tekanan rendah...");

        double baseWater = 1.0;

        if (plant.getStage().equalsIgnoreCase("Bibit")) {
            baseWater = 0.5;
            logger.info(">> [Drip] Mode: Tetesan Halus (Bibit).");
        } else {
            logger.info(">> [Drip] Mode: Tetesan Normal.");
        }

        // Fix: Use formatting
        logger.log(Level.INFO, ">> [Drip] Air menetes perlahan ke akar {0}", plant.getName());
        return baseWater;
    }
}