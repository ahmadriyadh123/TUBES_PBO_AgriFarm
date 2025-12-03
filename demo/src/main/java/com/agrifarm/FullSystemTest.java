package com.agrifarm;

import com.agrifarm.dao.*;
import com.agrifarm.model.*;
import com.agrifarm.service.*;
import com.agrifarm.manager.*;

import java.util.List;

public class FullSystemTest {

    public static void main(String[] args) {

        System.out.println("===== AGRI-FARM FULL SYSTEM TEST =====");

        // ===== 1. TEST KONEKSI DATABASE =====
        try {
            if (DatabaseConfig.getConnection() != null) {
                System.out.println("[OK] Database connected successfully!");
            }
        } catch (Exception e) {
            System.out.println("[FAIL] Database connection failed!");
            e.printStackTrace();
            return;
        }

        // ===== 2. TEST DAO + SERVICE =====
        FarmerService farmerService = new FarmerService();
        FieldService fieldService = new FieldService();
        IrrigationService irrigationService = new IrrigationService();

        System.out.println("\n--- Testing Farmer Registration ---");
        Farmer f = new Farmer("Rina Test", "Bandung");
        farmerService.registerFarmer(f);
        System.out.println("[OK] Farmer registered: " + f.getName());

        System.out.println("\n--- Testing Field Creation ---");
        Field field = new Field("Lahan Uji", 200);
        fieldService.createField(field);
        System.out.println("[OK] Field created: " + field.getName());

        System.out.println("\n--- Testing Plant Creation & Assignment ---");
        Plant p1 = new Plant("Padi", 10);
        Plant p2 = new Plant("Jagung", 15);

        fieldService.addPlantToField(field.getId(), p1);
        fieldService.addPlantToField(field.getId(), p2);

        System.out.println("[OK] Plants added to field: " + field.getName());

        // ===== 3. TEST FARM MANAGER =====
        System.out.println("\n--- Testing FarmManager (Singleton) ---");

        FarmManager fm1 = FarmManager.getInstance();
        FarmManager fm2 = FarmManager.getInstance();

        if (fm1 == fm2) {
            System.out.println("[OK] Singleton pattern works (1 instance only)");
        } else {
            System.out.println("[FAIL] Singleton pattern failed!");
        }

        fm1.registerFarmer(f);
        fm1.assignField(f, field);

        System.out.println("[OK] FarmManager operations executed");

        // ===== 4. TEST IRRIGATION BUSINESS LOGIC =====
        System.out.println("\n--- Testing Irrigate All Plants in Field ---");

        irrigationService.irrigateAllPlants(field.getId());

        System.out.println("[OK] Irrigation completed for field " + field.getName());

        // ===== 5. TEST IRRIGATION LOG =====
        System.out.println("\n--- Testing Irrigation Log by Farmer ---");

        List<IrrigationLog> logs = irrigationService.getLogsByFarmer(f.getId());
        for (IrrigationLog log : logs) {
            System.out.println("LOG -> Plant: " + log.getPlantId() +
                    ", Date: " + log.getDate() +
                    ", Method: " + log.getMethod());
        }

        System.out.println("[OK] Irrigation logs retrieved");

        // ===== 6. TEST REPORT GENERATION =====
        System.out.println("\n--- Testing Farm Report ---");
        fm1.generateReport();
        System.out.println("[OK] Report generated successfully!");

        System.out.println("\n===== TEST COMPLETED SUCCESSFULLY =====");
    }
}
