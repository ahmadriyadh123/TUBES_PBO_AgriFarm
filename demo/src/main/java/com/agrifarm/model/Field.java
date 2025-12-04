package com.agrifarm.model;

import com.agrifarm.farmer.IrrigationStrategy;
import java.util.ArrayList;
import java.util.List;

// [COMPOSITE] Implementasi Interface Component
public class Field implements FieldComponent {

    private int id;
    private String location;
    private double size;
    private String soilType;
    private String status;
    private Farmer owner;

    // [COMPOSITE] List untuk menyimpan anak (bisa Plant atau Field lain)
    private List<FieldComponent> children;

    // Constructor 1
    public Field(String location, double size) {
        this.location = location;
        this.size = size;
        this.soilType = "Gembur";
        this.status = "Available";
        this.children = new ArrayList<>(); // Inisialisasi List
    }

    // Constructor 2 (Full)
    public Field(int id, String location, double size, String soilType, String status) {
        this.id = id;
        this.location = location;
        this.size = size;
        this.soilType = soilType;
        this.status = status;
        this.children = new ArrayList<>(); // Inisialisasi List
    }

    // --- Getter & Setter Original ---
    public int getId() { return id; }
    public String getLocation() { return location; }
    public double getSize() { return size; }
    public String getSoilType() { return soilType; }
    public String getStatus() { return status; }
    public Farmer getOwner() { return owner; }
    public void setOwner(Farmer owner) { this.owner = owner; }

    // =========================================================
    // IMPLEMENTASI COMPOSITE (CONTAINER)
    // =========================================================

    // Menambah komponen (Tanaman/Sub-Lahan) ke dalam Lahan ini
    public void addComponent(FieldComponent component) {
        children.add(component);
    }

    // Menghapus komponen
    public void removeComponent(FieldComponent component) {
        children.remove(component);
    }

    // Mendapatkan daftar isi lahan
    public List<FieldComponent> getChildren() {
        return children;
    }

    @Override
    public void displayInfo() {
        System.out.println("[Lahan] ID: " + id + " | Lokasi: " + location + " (" + soilType + ")");
        // Delegasi: Tampilkan info semua anak
        if (children.isEmpty()) {
            System.out.println("    (Lahan kosong)");
        } else {
            for (FieldComponent child : children) {
                child.displayInfo();
            }
        }
    }

    @Override
    public double irrigate(IrrigationStrategy strategy) {
        double totalWaterUsed = 0;
        System.out.println(">> [Composite] Memulai irigasi massal untuk Lahan: " + location);

        // Delegasi: Perintahkan semua anak untuk disiram
        // Loop ini adalah inti dari "Keseragaman Operasi"
        for (FieldComponent child : children) {
            totalWaterUsed += child.irrigate(strategy);
        }

        return totalWaterUsed;
    }
}