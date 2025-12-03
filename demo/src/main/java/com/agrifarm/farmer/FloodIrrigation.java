package com.agrifarm.farmer;

import com.agrifarm.model.Plant;

public class FloodIrrigation implements IrrigationStrategy {
    @Override
    public double irrigate(Plant plant) {
        int growthBonus = 20;
        plant.grow(growthBonus);

        System.out.println(">> [Flood] Membuka pintu air irigasi...");
        System.out.println(">> [Flood] Menggenangi lahan (Metode Leb). Cocok untuk " + plant.getName());

        return 50.0;
    }
}