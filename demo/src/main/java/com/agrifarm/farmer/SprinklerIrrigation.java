package com.agrifarm.farmer;

import com.agrifarm.model.Plant;
import java.util.logging.Logger;

public class SprinklerIrrigation implements IrrigationStrategy {

    private static final Logger logger = Logger.getLogger(SprinklerIrrigation.class.getName());

    @Override
    public double irrigate(Plant plant) {
        int growthBonus = 15;
        plant.grow(growthBonus);

        logger.info(">> [Sprinkler] Mengaktifkan penyiram putar otomatis...");
        logger.info(">> [Sprinkler] Menyebarkan air secara merata ke seluruh lahan.");

        return 5.0;
    }
}