package com.agrifarm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Field implements IFieldComponent {

    private static final Logger logger = Logger.getLogger(Field.class.getName());

    private int id;
    private String location;
    private double size;
    private String soilType;
    private String status;
    private Farmer owner;

    private List<IFieldComponent> components = new ArrayList<>();

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

    public void addComponent(IFieldComponent component) {
        components.add(component);
    }

    public void removeComponent(IFieldComponent component) {
        components.remove(component);
    }

    public IFieldComponent getChild(int index) {
        return components.get(index);
    }

    public void displayInfo() {
        // Fix formatting
        logger.log(Level.INFO, ">> [Lahan] Lokasi: {0} ({1})", new Object[] { location, soilType });
        logger.info(">> Isi Komponen:");
        // Delegasi ke anak-anaknya
        for (IFieldComponent comp : components) {
            comp.displayInfo();
        }
    }

    @Override
    public double calculateYield() {
        double totalYield = 0;
        // Rekursif: Menjumlahkan yield dari semua children
        for (IFieldComponent comp : components) {
            totalYield += comp.calculateYield();
        }
        return totalYield;
    }

    public boolean isClaimed() {
        return owner != null || (status != null && !status.equalsIgnoreCase("Available"));
    }
}