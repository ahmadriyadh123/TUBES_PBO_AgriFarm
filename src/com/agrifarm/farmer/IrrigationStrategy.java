package com.agrifarm.farmer;

import com.agrifarm.model.Plant;

public interface IrrigationStrategy {
    void irrigate(Plant plant);
}