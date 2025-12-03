package com.agrifarm.farmer;

import com.agrifarm.model.Plant;

public interface IrrigationStrategy {
    double irrigate(Plant plant);
}