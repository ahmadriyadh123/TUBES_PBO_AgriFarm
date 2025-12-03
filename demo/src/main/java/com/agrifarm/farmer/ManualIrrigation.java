package com.agrifarm.farmer;

import com.agrifarm.model.Plant;

public class ManualIrrigation implements IrrigationStrategy {
    @Override
    public double irrigate(Plant plant) {
        int growthBonus = 10; 
        plant.grow(growthBonus);

        System.out.println(">> [Manual] Petani mengambil ember...");
        System.out.println(">> [Manual] Menyiram " + plant.getName() + " secara kasar.");
        
        double waterUsed = 10.0;
        
        System.out.println(">> [Info] Air terpakai: " + waterUsed + " Liter (Agak boros).");
        return waterUsed;
    }
}