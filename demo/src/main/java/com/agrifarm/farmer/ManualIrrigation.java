package com.agrifarm.farmer;

import com.agrifarm.model.Plant;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManualIrrigation implements IrrigationStrategy {

    private static final Logger logger = Logger.getLogger(ManualIrrigation.class.getName());

    @Override
    public double irrigate(Plant plant) {
        int growthBonus = 10;
        plant.grow(growthBonus);

        logger.info(">> [Manual] Petani mengambil ember...");
        logger.log(Level.INFO, ">> [Manual] Menyiram {0} secara kasar.", plant.getName());

        double waterUsed = 10.0;

        logger.log(Level.INFO, ">> [Info] Air terpakai: {0} Liter (Agak boros).", waterUsed);
        return waterUsed;
    }
}