package com.agrifarm;

import com.agrifarm.dao.*;
import com.agrifarm.model.*;
import com.agrifarm.service.*;
import com.agrifarm.manager.*;

import java.util.List;

public class FullSystemTest {

    public static void main(String[] args) {

        System.out.println("===== AGRI-FARM FULL SYSTEM TEST =====");

        // 1. SETUP DEPENDENCIES (DAO)
        FarmerDAO farmerDAO = new FarmerDAO();
        FieldDAO fieldDAO = new FieldDAO();
        PlantDAO plantDAO = new PlantDAO();
        IrrigationLogDAO irrigationLogDAO = new IrrigationLogDAO();

        // 2. SETUP SERVICES (Injeksi DAO ke Service)
        FarmerService farmerService = new FarmerService(farmerDAO, irrigationLogDAO);
        FieldService fieldService = new FieldService(fieldDAO);
        // IrrigationService membutuhkan PlantDAO dan IrrigationLogDAO
        IrrigationService irrigationService = new IrrigationService(plantDAO, irrigationLogDAO);

        // 3. TEST REGISTRASI FARMER
        System.out.println("\n--- Testing Farmer Registration ---");
        Farmer f = new Farmer("Rina Test", "password"); 
        // Simpan ke DB
        farmerService.registerFarmer(f);
        System.out.println("[OK] Farmer registered: " + f.getName());

        // 4. TEST FIELD CREATION
        System.out.println("\n--- Testing Field Creation ---");
        Field field = new Field("Lahan Uji", 200);
        // Menggunakan assignField (yang memanggil save di DAO)
        fieldService.assignField(field, f.getId()); 
        System.out.println("[OK] Field created at: " + field.getLocation());

        // 5. TEST PLANT & ASSIGNMENT
        System.out.println("\n--- Testing Plant Logic ---");
        // Gunakan konstruktor yang sesuai: (Nama, Fase)
        Plant p1 = new Plant("Padi", "Bibit"); 
        
        // Simpan tanaman via PlantDAO (karena FieldService belum punya logika ini di versi sebelumnya)
        plantDAO.save(p1, field.getId());
        System.out.println("[OK] Plant saved: " + p1.getName());

        // 6. TEST FARM MANAGER (Singleton)
        System.out.println("\n--- Testing FarmManager ---");
        FarmManager fm = FarmManager.getInstance();
        
        // Simulasi Assign Field (Menggunakan ID Dummy 1 & 1 karena kita tidak fetch ID asli dari DB di test ini)
        int dummyFarmerId = 1;
        int dummyFieldId = 1;
        fm.assignField(dummyFarmerId, dummyFieldId);

        // 7. TEST IRRIGATION LOGIC
        System.out.println("\n--- Testing Irrigation ---");
        // Service akan mencari tanaman di Field ID 1 dan mencatat log untuk Farmer ID 1
        irrigationService.irrigateField(dummyFieldId, dummyFarmerId);
        System.out.println("[OK] Irrigation process executed");

        // 8. TEST LOGS
        System.out.println("\n--- Testing Logs ---");
        List<IrrigationLog> logs = irrigationService.getAllLogs(); // Gunakan getAllLogs()
        for (IrrigationLog log : logs) {
            System.out.println("LOG -> FieldID: " + log.getFieldId() + 
                               ", Water: " + log.getWaterVolume() + 
                               ", Time: " + log.getTimestamp());
        }

        System.out.println("\n===== TEST COMPLETED =====");
    }
}