package com.agrifarm.farmer;

import com.agrifarm.model.Plant;

public class SprinklerIrrigation implements IrrigationStrategy {
    @Override
    public double irrigate(Plant plant) {
        int growthBonus = 15;
        plant.grow(growthBonus);
        
        System.out.println(">> [Sprinkler] Mengaktifkan penyiram putar otomatis...");
        System.out.println(">> [Sprinkler] Menyebarkan air secara merata ke seluruh lahan.");
        
        return 5.0;
    }
}