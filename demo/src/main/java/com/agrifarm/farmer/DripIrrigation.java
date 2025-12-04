package com.agrifarm.farmer;

import com.agrifarm.model.Plant;

public class DripIrrigation implements IrrigationStrategy {
    @Override
    public double irrigate(Plant plant) {
        int growthBonus = 25; 
        plant.grow(growthBonus);
        
        System.out.println(">> [Drip] Menyalakan pompa tekanan rendah...");
        
        double baseWater = 1.0;
        double efficiencyFactor = 0.95;
        
        if (plant.getStage().equalsIgnoreCase("Bibit")) {
            baseWater = 0.5;
            System.out.println(">> [Drip] Mode: Tetesan Halus (Bibit).");
        } else {
            System.out.println(">> [Drip] Mode: Tetesan Normal.");
        }

        System.out.println(">> [Drip] Air menetes perlahan ke akar " + plant.getName());
        return baseWater;
    }
}