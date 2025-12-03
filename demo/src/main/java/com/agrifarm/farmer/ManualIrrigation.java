package com.agrifarm.farmer;

import com.agrifarm.model.Plant;

public class ManualIrrigation implements IrrigationStrategy {
    @Override
    public void irrigate(Plant plant) {
        System.out.println("[Manual] Petani menyiram tanaman " + plant.getName() + " menggunakan ember/selang.");
    }
}