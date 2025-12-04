package com.agrifarm.model;

import com.agrifarm.farmer.IrrigationStrategy;

public interface FieldComponent {
    void displayInfo();
    double irrigate(IrrigationStrategy strategy);
}