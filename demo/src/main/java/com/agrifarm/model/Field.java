package com.agrifarm.model;

public class Field {

    private int id;
    private String location;
    private double size;
    private String soilType;
    private String status;
    private Farmer owner;

    public Field(String location, double size) {
        this.location = location;
        this.size = size;
        this.soilType = "Gembur";
        this.status = "Available";
    }

    public Field(int id, String location, double size, String soilType, String status) {
        this.id = id;
        this.location = location;
        this.size = size;
        this.soilType = soilType;
        this.status = status;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }
    
    public String getLocation() {
        return location;
    }

    public double getSize() {
        return size;
    }

    public String getSoilType() {
        return soilType;
    }

    public String getStatus() {
        return status;
    }

    public Farmer getOwner() {
        return owner;
    }

    public void setOwner(Farmer owner) {
        this.owner = owner;
    }
}   