package com.agrifarm.model;

import java.time.LocalDate;

import com.agrifarm.farmer.IrrigationStrategy;
import com.agrifarm.farmer.ManualIrrigation;

public class Plant implements IFieldComponent {

    private int id;
    private final String name;
    private String type;
    private String growthStage;
    private LocalDate estimatedHarvestDate;
    private String requiredSoilType;
    private int growthProgress = 0;

    private double estimatedYield;

    private IrrigationStrategy activeStrategy;

    public Plant(String name, String growthStage) {
        this.name = name;
        this.growthStage = growthStage;
        this.type = "Umum";
        this.estimatedHarvestDate = LocalDate.now().plusMonths(3);
        this.requiredSoilType = determineRequiredSoil(name);
        this.activeStrategy = new ManualIrrigation();
        this.estimatedYield = generateYieldPrediction();
    }

    public Plant(int id, String name, String type, String growthStage, LocalDate estimatedHarvestDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.growthStage = growthStage;
        this.estimatedHarvestDate = estimatedHarvestDate;
        this.requiredSoilType = determineRequiredSoil(name);
        this.activeStrategy = new ManualIrrigation();
        this.estimatedYield = generateYieldPrediction();
    }

    private String determineRequiredSoil(String name) {
        String lowerName = name.toLowerCase();
        if (lowerName.contains("padi")) {
            return "Tanah Liat"; // Padi butuh air menggenang
        } else if (lowerName.contains("jagung") || lowerName.contains("cabai") || lowerName.contains("tomat")) {
            return "Tanah Gembur"; // Umumnya tanaman butuh tanah subur/gembur
        } else if (lowerName.contains("kaktus") || lowerName.contains("kurma")) {
            return "Tanah Pasir"; // Tanaman gurun
        } else {
            return "Tanah Gembur"; // Default untuk tanaman umum
        }
    }
    
    private double generateYieldPrediction() {
        // Simulasi logika bisnis: Padi ~5kg/rumpun, Cabai ~1kg/pohon
        if (name.toLowerCase().contains("padi")) return 5.0;
        if (name.toLowerCase().contains("cabai")) return 1.5;
        if (name.toLowerCase().contains("jagung")) return 2.0;
        return 1.0; // Default
    }

    // --- IMPLEMENTASI COMPOSITE (LEAF) ---

    @Override
    public void displayInfo() {
        System.out.println("   -> [Tanaman] " + name + " | Estimasi: " + estimatedYield + " kg");
    }

    @Override
    public double calculateYield() {
        // Leaf mengembalikan nilai miliknya sendiri
        return this.estimatedYield;
    }

    public boolean isCompatibleWithSoil(String fieldSoilType) {
        return this.requiredSoilType.equalsIgnoreCase(fieldSoilType);
    }

    public String getName() {
        return name;
    }

    public String getStage() {
        return growthStage;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setStrategy(IrrigationStrategy strategy) {
        this.activeStrategy = strategy;
    }

    public IrrigationStrategy getStrategy() {
        return this.activeStrategy;
    }

    public String getGrowthStage() {
        return growthStage;
    }

    public LocalDate getEstimatedHarvestDate() {
        return estimatedHarvestDate;
    }

    public String getRequiredSoilType() { return requiredSoilType; }

    public int getProgress() {
        return growthProgress;
    }

    public void grow(int percent) {
        this.growthProgress += percent;
        if (this.growthProgress > 100) this.growthProgress = 100;
    }

    public boolean isHarvestReady() {
        return growthProgress >= 100;
    }
}
