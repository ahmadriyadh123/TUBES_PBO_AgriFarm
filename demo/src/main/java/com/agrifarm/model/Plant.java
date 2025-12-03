package com.agrifarm.model;

import java.time.LocalDate;

public class Plant {

    private int id;
    private final String name;
    private String type;
    private String growthStage;
    private LocalDate estimatedHarvestDate;
    private String requiredSoilType;

    public Plant(String name, String growthStage) {
        this.name = name;
        this.growthStage = growthStage;
        this.type = "Umum";
        this.estimatedHarvestDate = LocalDate.now().plusMonths(3);
        this.requiredSoilType = determineRequiredSoil(name);
    }

    public Plant(int id, String name, String type, String growthStage, LocalDate estimatedHarvestDate) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.growthStage = growthStage;
        this.estimatedHarvestDate = estimatedHarvestDate;
        this.requiredSoilType = determineRequiredSoil(name);
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

    public String getGrowthStage() {
        return growthStage;
    }

    public LocalDate getEstimatedHarvestDate() {
        return estimatedHarvestDate;
    }

    public String getRequiredSoilType() { return requiredSoilType; }
}
