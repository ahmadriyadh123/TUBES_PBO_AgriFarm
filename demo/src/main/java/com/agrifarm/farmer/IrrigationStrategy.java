package demo.src.com.agrifarm.farmer;

import demo.src.com.agrifarm.model.Plant;

public interface IrrigationStrategy {
    void irrigate(Plant plant);
}