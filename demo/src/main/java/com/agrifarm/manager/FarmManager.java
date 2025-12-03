package com.agrifarm.manager;

import com.agrifarm.dao.*;
import com.agrifarm.model.*;

public class FarmManager {

    // ==== Singleton Instance ====
    private static FarmManager instance;

    // ==== DAO Dependencies ====
    private FarmerDAO farmerDAO;
    private FieldDAO fieldDAO;
    private PlantDAO plantDAO;
    private IrrigationLogDAO irrigationLogDAO;

    // ==== Private Constructor ====
    private FarmManager() {
        this.farmerDAO = new FarmerDAO();
        this.fieldDAO = new FieldDAO();
        this.plantDAO = new PlantDAO();
        this.irrigationLogDAO = new IrrigationLogDAO();
    }

    // ==== Mendapatkan Instance Singleton ====
    public static synchronized FarmManager getInstance() {
        if (instance == null) {
            instance = new FarmManager();
        }
        return instance;
    }


    // ============================================================
    // =============== FUNGSI BISNIS (Business Logic) ===============
    // ============================================================

    // ---- Register Farmer ----
    public void registerFarmer(Farmer farmer) {
        farmerDAO.save(farmer);
        System.out.println("✔ Farmer registered: " + farmer.getName());
    }

    // ---- Menambah Lahan ----
    public void addField(Field field, int farmerId) {
        fieldDAO.save(field, farmerId);
        System.out.println("✔ Field added: " + field.getLocation());
    }

    // ---- Menambah Plant ----
    public void addPlant(Plant plant, int fieldId) {
        plantDAO.save(plant, fieldId);
        System.out.println("✔ Plant added: " + plant.getName());
    }

    // ---- Assign Field ke Farmer ----
    public void assignField(int farmerId, int fieldId) {
        fieldDAO.assignField(farmerId, fieldId);
        System.out.println("✔ Field " + fieldId + " assigned to Farmer " + farmerId);
    }

    // ---- Menyimpan Log Penyiraman ----
    public void logIrrigation(int fieldId, double water) {
        IrrigationLog log = new IrrigationLog(fieldId, water);
        irrigationLogDAO.save(log);

        System.out.println("✔ Irrigation logged for Field " + fieldId);
    }

    // ---- Generate Report (Contoh Sederhana) ----
    public void generateReport() {
        System.out.println("====== FARM REPORT ======");
        System.out.println("=========================");
    }
}
