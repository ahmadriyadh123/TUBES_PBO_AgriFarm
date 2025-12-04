package com.agrifarm.manager;

import com.agrifarm.dao.*;
import com.agrifarm.model.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("java:S6548") // [FIX] Mengatasi warning Singleton (S6548) karena ini disengaja
public class FarmManager {

    // [FIX] Mengganti System.out dengan Logger (S106)
    private static final Logger LOGGER = Logger.getLogger(FarmManager.class.getName());

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
        // [FIX] Gunakan Logger
        LOGGER.log(Level.INFO, "Farmer registered: {0}", farmer.getName());
    }

    // ---- Menambah Lahan ----
    public void addField(Field field, int farmerId) {
        fieldDAO.save(field, farmerId);
        LOGGER.log(Level.INFO, "Field added: {0}", field.getLocation());
    }

    // ---- Menambah Plant ----
    public void addPlant(Plant plant) {
        plantDAO.save(plant); // Asumsi save sudah diupdate di DAO sebelumnya
        LOGGER.log(Level.INFO, "Plant added: {0}", plant.getName());
    }

    // ---- Assign Field ke Farmer ----
    public void assignField(int farmerId, int fieldId) {
        fieldDAO.assignField(farmerId, fieldId);
        LOGGER.log(Level.INFO, "Field {0} assigned to Farmer {1}", new Object[]{fieldId, farmerId});
    }

    // ---- Menyimpan Log Penyiraman ----
    public void logIrrigation(int fieldId, double water) {
        IrrigationLog log = new IrrigationLog(fieldId, water);
        irrigationLogDAO.save(log);

        LOGGER.log(Level.INFO, "Irrigation logged for Field {0}", fieldId);
    }

    // ---- Generate Report (Contoh Sederhana) ----
    public void generateReport() {
        LOGGER.info("====== FARM REPORT ======");
        LOGGER.info("=========================");
    }
}