package com.agrifarm.model;

import java.time.LocalDate;

public class Plant {

    private int id;
    private final String name;
    private String type;
    private String growthStage;
    private LocalDate estimatedHarvestDate;

    public Plant(String name, String growthStage) {
        this.name = name;
        this.growthStage = growthStage;
        this.type = "Umum";
        this.estimatedHarvestDate = LocalDate.now().plusMonths(3);
    }

    public Plant(int id, String name, String type, String growthStage, LocalDate estimatedHarvestDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.growthStage = growthStage;
        this.estimatedHarvestDate = estimatedHarvestDate;
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

    public String getGrowthStage() {
        return growthStage;
    }

    public LocalDate getEstimatedHarvestDate() {
        return estimatedHarvestDate;
    }
}
