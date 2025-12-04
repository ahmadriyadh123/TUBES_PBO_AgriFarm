package com.agrifarm.model;

import com.agrifarm.farmer.IrrigationStrategy;
import java.time.LocalDate;

// [COMPOSITE] Implementasi Interface Component
public class Plant implements FieldComponent {

    private int id;
    private final String name;
    private String type;
    private String growthStage;
    private LocalDate estimatedHarvestDate;
    private String requiredSoilType;
    private int growthProgress = 0;

    // Constructor 1
    public Plant(String name, String growthStage) {
        this.name = name;
        this.growthStage = growthStage;
        this.type = "Umum";
        this.estimatedHarvestDate = LocalDate.now().plusMonths(3);
        this.requiredSoilType = determineRequiredSoil(name);
    }

    // Constructor 2 (Full)
    public Plant(int id, String name, String type, String growthStage, LocalDate estimatedHarvestDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.growthStage = growthStage;
        this.estimatedHarvestDate = estimatedHarvestDate;
        this.requiredSoilType = determineRequiredSoil(name);
    }

    // Logic penentuan tanah
    private String determineRequiredSoil(String name) {
        String lowerName = name.toLowerCase();
        if (lowerName.contains("padi")) return "Tanah Liat";
        else if (lowerName.contains("kaktus") || lowerName.contains("kurma")) return "Tanah Pasir";
        else return "Tanah Gembur";
    }

    // --- Getter & Setter Original ---
    public int getId() { return id; }
    public String getName() { return name; }
    public String getStage() { return growthStage; }
    public String getType() { return type; }
    public String getGrowthStage() { return growthStage; }
    public LocalDate getEstimatedHarvestDate() { return estimatedHarvestDate; }
    public String getRequiredSoilType() { return requiredSoilType; }
    public int getProgress() { return growthProgress; }

    public void grow(int percent) {
        this.growthProgress += percent;
        if (this.growthProgress > 100) this.growthProgress = 100;
    }

    public boolean isHarvestReady() {
        return growthProgress >= 100;
    }

    // =========================================================
    // IMPLEMENTASI COMPOSITE (LEAF)
    // =========================================================

    @Override
    public void displayInfo() {
        System.out.println("    -> [Tanaman] " + name + " | Fase: " + growthStage + " | Progress: " + growthProgress + "%");
    }

    @Override
    public double irrigate(IrrigationStrategy strategy) {
        // Leaf langsung meminta strategy untuk menyiram dirinya sendiri
        return strategy.irrigate(this);
    }
}