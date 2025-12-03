package com.agrifarm.model;

public class Plant {
    private String name;
    private String growthStage;

    public Plant(String name, String growthStage) {
        this.name = name;
        this.growthStage = growthStage;
    }

    public String getName() {
        return name;
    }

    public String getGrowthStage() {
        return growthStage;
    }
}