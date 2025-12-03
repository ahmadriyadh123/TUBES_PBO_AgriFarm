package com.agrifarm.farmer;

public class AutomaticIrrigation implements IrrigationStrategy {
    @Override
    public void irrigate(Plant plant) {
        System.out.println("Sistem sensor mendeteksi kelembapan. Sprinkler menyiram tanaman " + plant.getName() + " secara otomatis.");
    }
}
