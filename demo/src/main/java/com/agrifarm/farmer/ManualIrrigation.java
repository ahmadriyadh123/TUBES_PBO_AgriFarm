package demo.src.com.agrifarm.farmer;

import demo.src.com.agrifarm.model.Plant;

public class ManualIrrigation implements IrrigationStrategy {
    @Override
    public void irrigate(Plant plant) {
        System.out.println("[Manual] Petani menyiram tanaman " + plant.getName() + " menggunakan ember/selang.");
    }
}