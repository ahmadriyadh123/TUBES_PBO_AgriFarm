package com.agrifarm.farmer;

import com.agrifarm.model.Plant;

public class DripIrrigation implements IrrigationStrategy {
    @Override
    public void irrigate(Plant plant) {
        System.out.println("Air diteteskan perlahan ke akar tanaman " + plant.getName() + " secara perlahan dan efisien.");
    }
}