package com.agrifarm.main;

import java.util.List;

import com.agrifarm.model.*;
import com.agrifarm.dao.*;
import com.agrifarm.farmer.DripIrrigation;
import com.agrifarm.farmer.FloodIrrigation;
import com.agrifarm.farmer.SprinklerIrrigation;
import com.agrifarm.service.IrrigationService;
import com.agrifarm.utils.ConsoleUI;

public class Main {
    private static final FarmerDAO farmerDAO = new FarmerDAO();
    private static final FieldDAO fieldDAO = new FieldDAO();
    private static final PlantDAO plantDAO = new PlantDAO();
    private static final IrrigationLogDAO logDAO = new IrrigationLogDAO();

    @SuppressWarnings("unused")
    private static final IrrigationService irrigationService = new IrrigationService(logDAO, plantDAO);

    // Menu Constants
    private static final String MENU_CLAIM_LAHAN = "1";
    private static final String MENU_TANAM_BIBIT = "2";
    private static final String MENU_PILIH_TANAMAN = "3";
    private static final String MENU_SIRAM_TARGET = "4";
    private static final String MENU_LIHAT_RIWAYAT = "5";
    private static final String MENU_STATISTIK_AIR = "6";
    private static final String MENU_ESTIMASI_PANEN = "7";
    private static final String MENU_LOGOUT = "8";

    // Soil Constants
    private static final String SOIL_LIAT = "Tanah Liat";
    private static final String SOIL_PASIR = "Tanah Pasir";
    private static final String SOIL_GEMBUR = "Tanah Gembur";
    private static final String SOIL_LEMPUNG = "Tanah Lempung";
    private static final String SOIL_GAMBUT = "Tanah Gambut";

    public static void main(String[] args) {
        try (ConsoleUI ui = new ConsoleUI()) {
            boolean appRunning = true;

            while (appRunning) {
                printMainMenu(ui);
                String menuAwal = ui.readLine();

                switch (menuAwal) {
                    case "1":
                        handleLoginFlow(ui);
                        break;
                    case "2":
                        handleRegister(ui);
                        break;
                    case "3":
                        ui.println("Sampai jumpa!");
                        appRunning = false;
                        break;
                    default:
                        ui.println(">> Pilihan tidak dikenal.");
                        break;
                }
            }
        }
    }

    private static void printMainMenu(ConsoleUI ui) {
        ui.println("\n=================================");
        ui.println("            AGRI-FARM ");
        ui.println("=================================");
        ui.println("1. Login");
        ui.println("2. Registrasi");
        ui.println("3. Keluar");
        ui.print("Pilih menu: ");
    }

    private static void handleLoginFlow(ConsoleUI ui) {
        Farmer loggedInFarmer = handleLogin(ui);
        if (loggedInFarmer != null) {
            runDashboard(ui, loggedInFarmer);
        }
    }

    private static void runDashboard(ConsoleUI ui, Farmer pakTani) {
        boolean sessionRunning = true;
        Plant selectedTargetPlant = null;

        while (sessionRunning) {
            Field myField = fieldDAO.getByFarmer(pakTani.getId());
            List<Plant> allPlants = (myField != null) ? plantDAO.getByField(myField.getId()) : null;

            displayDashboardInfo(ui, pakTani, myField, allPlants, selectedTargetPlant);
            printDashboardMenu(ui);

            String pilihan = ui.readLine();

            switch (pilihan) {
                case MENU_CLAIM_LAHAN:
                    handleClaimField(ui, pakTani, myField);
                    break;
                case MENU_TANAM_BIBIT:
                    handlePlantSeed(ui, myField);
                    break;
                case MENU_PILIH_TANAMAN:
                    selectedTargetPlant = handleSelectPlant(ui, pakTani, allPlants);
                    break;
                case MENU_SIRAM_TARGET:
                    handleIrrigateTarget(ui, pakTani, selectedTargetPlant);
                    break;
                case MENU_LIHAT_RIWAYAT:
                    showIrrigationHistory(pakTani);
                    break;
                case MENU_STATISTIK_AIR:
                    showWaterUsageStats(pakTani);
                    break;
                case MENU_ESTIMASI_PANEN:
                    if (myField != null) { // Pass myField here
                        showHarvestEstimation(ui, myField);
                    } else {
                        ui.println(">> Belum punya lahan.");
                    }
                    break;
                case MENU_LOGOUT:
                    sessionRunning = false;
                    break;
                default:
                    ui.println(">> Pilihan tidak valid.");
                    break;
            }
        }
    }

    private static void displayDashboardInfo(ConsoleUI ui, Farmer pakTani, Field myField, List<Plant> allPlants,
            Plant selectedTargetPlant) {
        ui.println("\n---------------------------------");
        ui.println("DASHBOARD PETANI: " + pakTani.getName().toUpperCase());
        ui.println("---------------------------------");

        String fieldInfo = (myField != null)
                ? myField.getLocation() + " (" + myField.getSoilType() + ")"
                : "[BELUM ADA]";
        ui.println("Lahan        : " + fieldInfo);

        if (allPlants != null && !allPlants.isEmpty()) {
            ui.println("Total Tanaman: " + allPlants.size() + " Jenis");
            ui.print("Daftar       : ");
            for (Plant p : allPlants) {
                ui.print("[" + p.getName() + "] ");
            }
            ui.println();
        } else {
            ui.println("Tanaman      : [KOSONG]");
        }

        String targetName = (selectedTargetPlant != null)
                ? ">> " + selectedTargetPlant.getName() + " <<"
                : "[BELUM DIPILIH]";
        ui.println("Target Aksi  : " + targetName);
    }

    private static void printDashboardMenu(ConsoleUI ui) {
        ui.println("---------------------------------");
        ui.println("1. Klaim Lahan Baru");
        ui.println("2. Tanam Bibit");
        ui.println("3. Pilih Tanaman & Cek Strategi Irigasi");
        ui.println("4. Siram Target Terpilih");
        ui.println("5. Lihat Riwayat Irigasi");
        ui.println("6. Laporan Statistik Air");
        ui.println("7. Cek Estimasi Panen");
        ui.println("8. Logout");
        ui.print("Pilih: ");
    }

    private static void determineAndSetStrategy(ConsoleUI ui, Farmer farmer, Plant plant) {
        ui.println("\n>> [ANALISIS] Menganalisis " + plant.getName() + "...");

        String plantName = plant.getName().toLowerCase();
        String plantStage = plant.getStage();

        if (plantName.contains("padi")) {
            ui.println(">> Tipe: Padi -> Butuh Flood Irrigation.");
            farmer.setStrategy(new FloodIrrigation());
        } else if (plantStage.equalsIgnoreCase("Bibit")) {
            ui.println(">> Tipe: Bibit -> Butuh Drip Irrigation.");
            farmer.setStrategy(new DripIrrigation());
        } else {
            ui.println(">> Tipe: Umum -> Menggunakan Sprinkler Irrigation.");
            farmer.setStrategy(new SprinklerIrrigation());
        }
        ui.println(">> SIAP MENYIRAM: " + plant.getName());
    }

    private static void handleClaimField(ConsoleUI ui, Farmer pakTani, Field myField) {
        if (myField != null) {
            ui.println(">> Anda sudah memiliki lahan (" + myField.getSoilType() + ").");
            return;
        }

        ui.println("\n--- PENDAFTARAN LAHAN BARU ---");
        ui.print("Lokasi Lahan : ");
        String loc = ui.readLine();

        String soilType = selectSoilType(ui);

        Field f = new Field(0, loc, 100, soilType, "Ready");

        fieldDAO.save(f, pakTani.getId());
        ui.println(">> Lahan di " + loc + " dengan jenis " + soilType + " berhasil disimpan!");
    }

    private static String selectSoilType(ConsoleUI ui) {
        ui.println("Pilih Jenis Tanah:");
        ui.println("1. " + SOIL_LIAT);
        ui.println("2. " + SOIL_PASIR);
        ui.println("3. " + SOIL_GEMBUR);
        ui.println("4. " + SOIL_LEMPUNG);
        ui.println("5. " + SOIL_GAMBUT);
        ui.print("Pilihan (1-5): ");
        String soilChoice = ui.readLine();

        switch (soilChoice) {
            case "1":
                return SOIL_LIAT;
            case "2":
                return SOIL_PASIR;
            case "3":
                return SOIL_GEMBUR;
            case "4":
                return SOIL_LEMPUNG;
            case "5":
                return SOIL_GAMBUT;
            default:
                ui.println(">> Pilihan tidak valid! Menggunakan default (Gembur).");
                return SOIL_GEMBUR;
        }
    }

    private static void handlePlantSeed(ConsoleUI ui, Field myField) {
        if (myField == null) {
            ui.println(">> Harap klaim lahan terlebih dahulu (Menu 1).");
            return;
        }

        ui.print("Nama Tanaman (cth: Padi, Jagung, Cabai): ");
        String pName = ui.readLine();

        Plant newPlant = new Plant(pName, "Bibit");

        plantDAO.save(newPlant, myField.getId());
        ui.println(">> " + pName + " berhasil ditanam.");
    }

    private static Plant handleSelectPlant(ConsoleUI ui, Farmer pakTani, List<Plant> allPlants) {
        if (allPlants == null || allPlants.isEmpty()) {
            ui.println(">> Tidak ada tanaman di lahan ini.");
            return null;
        }

        ui.println("\n--- PILIH TANAMAN UNTUK IRIGASI ---");

        for (int i = 0; i < allPlants.size(); i++) {
            Plant p = allPlants.get(i);
            ui.println((i + 1) + ". " + p.getName() + " (Fase: " + p.getStage() + ")");
        }

        ui.print("Pilih Nomor Tanaman: ");
        int choice = ui.readIntSafe();
        int idx = choice - 1;

        if (idx >= 0 && idx < allPlants.size()) {
            Plant selectedTargetPlant = allPlants.get(idx);
            determineAndSetStrategy(ui, pakTani, selectedTargetPlant);
            return selectedTargetPlant;
        } else {
            ui.println(">> Pilihan salah.");
            return null;
        }
    }

    private static void handleIrrigateTarget(ConsoleUI ui, Farmer pakTani, Plant selectedTargetPlant) {
        if (selectedTargetPlant != null) {
            pakTani.performIrrigation(selectedTargetPlant);
        } else {
            ui.println(">> Pilih tanaman dulu.");
        }
    }

    private static void showIrrigationHistory(Farmer pakTani) {
        pakTani.showHistory();
    }

    private static void showWaterUsageStats(Farmer pakTani) {
        pakTani.showWaterUsageStats();
    }

    private static void showHarvestEstimation(ConsoleUI ui, Field myField) {
        if (myField.isClaimed()) {
            // Replaced CompositeField with direct call since Field implements
            // IFieldComponent
            double yield = myField.calculateYield();
            ui.println(">> Estimasi Total Panen di Lahan: " + yield + " kg");
        } else {
            ui.println(">> Lahan belum di-claim.");
        }
    }

    private static void handleRegister(ConsoleUI ui) {
        ui.print("Username: ");
        String u = ui.readLine();
        ui.print("Password: ");
        String p = ui.readLine();
        Farmer f = new Farmer(u, p);
        farmerDAO.save(f);
    }

    private static Farmer handleLogin(ConsoleUI ui) {
        ui.print("Username: ");
        String u = ui.readLine();
        ui.print("Password: ");
        String p = ui.readLine();
        return farmerDAO.login(u, p);
    }
}