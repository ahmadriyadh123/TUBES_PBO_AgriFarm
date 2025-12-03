package demo.src.com.agrifarm.farmer;

import demo.src.com.agrifarm.model.Plant;

public class Farmer {
    private String name;
    private int experience;
    private IrrigationStrategy strategy;

    public Farmer(String name, int experience) {
        this.name = name;
        this.experience = experience;
        this.strategy = new ManualIrrigation();
    }

    public void setStrategy(IrrigationStrategy strategy) {
        this.strategy = strategy;
        System.out.println(">> " + this.name + " mengubah strategi irigasi.");
    }

    public void performIrrigation(Plant plant) {
        if (this.strategy == null) {
            System.out.println("Strategi belum dipilh");
        } else {
            System.out.println("Petani " + this.name + " (" + this.experience + " thn pengalaman): ");
            this.strategy.irrigate(plant);
        }
    }
}
