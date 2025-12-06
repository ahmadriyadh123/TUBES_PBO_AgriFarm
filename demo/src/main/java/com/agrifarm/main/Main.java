package com.agrifarm.main;

import java.util.Scanner;
import java.util.List;
import java.util.Map;

import com.agrifarm.model.*;
import com.agrifarm.dao.*;
import com.agrifarm.farmer.*;
import com.agrifarm.service.IrrigationService;

public class Main {
    private static FarmerDAO farmerDAO = new FarmerDAO();
    private static FieldDAO fieldDAO = new FieldDAO();

    private static PlantDAO plantDAO = new PlantDAO();
    private static IrrigationLogDAO logDAO = new IrrigationLogDAO();

    private static IrrigationService irrigationService = new IrrigationService(logDAO, plantDAO);

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean appRunning = true;

        while (appRunning) {
            System.out.println("\n=================================");
            System.out.println("            AGRI-FARM ");
            System.out.println("=================================");
            System.out.println("1. Login");
            System.out.println("2. Registrasi");
            System.out.println("3. Keluar");
            System.out.print("Pilih menu: ");

            String menuAwal = input.nextLine();

            if (menuAwal.equals("1")) {
                Farmer loggedInFarmer = handleLogin(input);
                if (loggedInFarmer != null) {
                    runDashboard(input, loggedInFarmer);
                }
            } else if (menuAwal.equals("2")) {
                handleRegister(input);
            } else if (menuAwal.equals("3")) {
                System.out.println("Sampai jumpa!");
                appRunning = false;
            }
        }
        input.close();
    }

    private static void runDashboard(Scanner input, Farmer pakTani) {
        boolean sessionRunning = true;
        Plant selectedTargetPlant = null;
        while (sessionRunning) {
            Field myField = fieldDAO.getByFarmer(pakTani.getId());

            List<Plant> allPlants = null;
            if (myField != null) {
                allPlants = plantDAO.getByField(myField.getId());
            }

            System.out.println("\n---------------------------------");
            System.out.println("DASHBOARD PETANI: " + pakTani.getName().toUpperCase());
            System.out.println("---------------------------------");
            System.out.println("Lahan        : "
                    + (myField != null ? myField.getLocation() + " (" + myField.getSoilType() + ")" : "[BELUM ADA]"));

            if (allPlants != null && !allPlants.isEmpty()) {
                System.out.println("Total Tanaman: " + allPlants.size() + " Jenis");
                System.out.print("Daftar       : ");
                for (Plant p : allPlants)
                    System.out.print("[" + p.getName() + "] ");
                System.out.println();
            } else {
                System.out.println("Tanaman      : [KOSONG]");
            }

            if (selectedTargetPlant != null) {
                System.out.println("Target Aksi  : >> " + selectedTargetPlant.getName() + " <<");
            } else {
                System.out.println("Target Aksi  : [BELUM DIPILIH]");
            }

            System.out.println("---------------------------------");
            System.out.println("1. Klaim Lahan Baru");
            System.out.println("2. Tanam Bibit");
            System.out.println("3. Pilih Tanaman & Cek Strategi Irigasi");
            System.out.println("4. Siram Target Terpilih");
            System.out.println("5. Lihat Riwayat Irigasi");
            System.out.println("6. Laporan Statistik Air");
            System.out.println("7. Cek Estimasi Panen");
            System.out.println("8. Logout");
            System.out.print("Pilih: ");

            String pilihan = input.nextLine();

            switch (pilihan) {
                case "1":
                    if (myField == null) {
                        System.out.println("\n--- PENDAFTARAN LAHAN BARU ---");
                        System.out.print("Lokasi Lahan : ");
                        String loc = input.nextLine();

                        System.out.println("Pilih Jenis Tanah:");
                        System.out.println("1. Tanah Liat");
                        System.out.println("2. Tanah Pasir");
                        System.out.println("3. Tanah Gembur");
                        System.out.println("4. Tanah Lempung");
                        System.out.println("5. Tanah Gambut");
                        System.out.print("Pilihan (1-5): ");
                        String soilChoice = input.nextLine();

                        String soilType = "Tanah Gembur";

                        switch (soilChoice) {
                            case "1":
                                soilType = "Tanah Liat";
                                break;
                            case "2":
                                soilType = "Tanah Pasir";
                                break;
                            case "3":
                                soilType = "Tanah Gembur";
                                break;
                            case "4":
                                soilType = "Tanah Lempung";
                                break;
                            case "5":
                                soilType = "Tanah Gambut";
                                break;
                            default:
                                System.out.println(">> Pilihan tidak valid! Menggunakan default (Gembur).");
                                break;
                        }

                        Field f = new Field(0, loc, 100, soilType, "Ready");

                        fieldDAO.save(f, pakTani.getId());
                        System.out.println(">> Lahan di " + loc + " dengan jenis " + soilType + " berhasil disimpan!");

                    } else {
                        System.out.println(">> Anda sudah memiliki lahan (" + myField.getSoilType() + ").");
                    }
                    break;

                case "2":
                    if (myField != null) {
                        System.out.print("Nama Tanaman (cth: Padi, Jagung, Cabai): ");
                        String pName = input.nextLine();

                        Plant newPlant = new Plant(pName, "Bibit");

                        plantDAO.save(newPlant, myField.getId());
                        System.out.println(">> " + pName + " berhasil ditanam.");
                    } else {
                        System.out.println(">> Harap klaim lahan terlebih dahulu (Menu 1).");
                    }
                    break;

                case "3":
                    if (allPlants == null || allPlants.isEmpty()) {
                        System.out.println(">> Tidak ada tanaman di lahan ini.");
                    } else {
                        System.out.println("\n--- PILIH TANAMAN UNTUK IRIGASI ---");

                        for (int i = 0; i < allPlants.size(); i++) {
                            Plant p = allPlants.get(i);
                            System.out.println((i + 1) + ". " + p.getName() + " (Fase: " + p.getStage() + ")");
                        }

                        System.out.print("Pilih Nomor Tanaman: ");
                        try {
                            int idx = Integer.parseInt(input.nextLine()) - 1;

                            if (idx >= 0 && idx < allPlants.size()) {

                                selectedTargetPlant = allPlants.get(idx);

                                System.out.println(
                                        "\n>> [ANALISIS] Menganalisis " + selectedTargetPlant.getName() + "...");

                                String pName = selectedTargetPlant.getName().toLowerCase();
                                String pStage = selectedTargetPlant.getStage();

                                if (pName.contains("padi")) {
                                    System.out.println(">> Tipe: Padi -> Butuh Flood Irrigation.");
                                    pakTani.setStrategy(new FloodIrrigation());
                                } else if (pStage.equalsIgnoreCase("Bibit")) {
                                    System.out.println(">> Tipe: Bibit -> Butuh Drip Irrigation.");
                                    pakTani.setStrategy(new DripIrrigation());
                                } else {
                                    System.out.println(">> Tipe: Umum -> Menggunakan Sprinkler.");
                                    pakTani.setStrategy(new SprinklerIrrigation());
                                }
                                System.out.println(">> SIAP MENYIRAM: " + selectedTargetPlant.getName());

                            } else {
                                System.out.println(">> Pilihan salah.");
                                selectedTargetPlant = null;
                            }
                        } catch (Exception e) {
                            System.out.println(">> Input harus angka.");
                        }
                    }
                    break;

                case "4":
                    if (selectedTargetPlant != null && myField != null) {
                        System.out.println("\n>> [EKSEKUSI] Menyiram tanaman: " + selectedTargetPlant.getName());

                        double volume = pakTani.executeIrrigation(selectedTargetPlant);

                        if (volume > 0) {

                            IrrigationLog log = new IrrigationLog(myField.getId(), volume);
                            irrigationService.create(log);
                            System.out.println(">> Data tersimpan untuk " + selectedTargetPlant.getName());
                        }
                    } else {
                        System.out.println(">> [ERROR] Belum ada tanaman yang dipilih!");
                        System.out.println(">> Silakan pilih menu nomor 3 terlebih dahulu.");
                    }
                    break;

                case "5":
                    List<IrrigationLog> history = irrigationService.getSortedLogsByDate(pakTani.getId());
                    System.out.println("\n=== RIWAYAT IRIGASI (Terbaru) ===");
                    if (history.isEmpty())
                        System.out.println("Belum ada data.");

                    for (IrrigationLog log : history) {
                        System.out.println("- [" + log.getTimestamp() + "] Vol: " + log.getWaterVolume() + "L (Field "
                                + log.getFieldId() + ")");
                    }
                    break;

                case "6":
                    Map<Integer, Double> stats = irrigationService.getWaterUsageStats(pakTani.getId());
                    System.out.println("\n=== STATISTIK PENGGUNAAN AIR ===");
                    if (stats.isEmpty())
                        System.out.println("Belum ada data.");

                    for (Map.Entry<Integer, Double> entry : stats.entrySet()) {
                        System.out.println(">> Lahan ID " + entry.getKey() + " : Total " + entry.getValue() + " Liter");
                    }
                    break;

                case "7":
                    if (myField != null) {
                        Field compositeField = myField;
                        List<Plant> dbPlants = plantDAO.getByField(compositeField.getId());
                        for (Plant p : dbPlants) {
                            compositeField.addComponent(p);
                        }

                        System.out.println("\n=== LAPORAN ESTIMASI PANEN (COMPOSITE PATTERN) ===");
                        compositeField.displayInfo();
                        System.out.println("---------------------------------------------");
                        double total = compositeField.calculateYield();
                        System.out.println(">> Total Potensi Hasil Panen: " + total + " kg");
                        System.out.println("=============================================");
                    } else {
                        System.out.println(">> Belum ada lahan.");
                    }
                    break;
                case "8":
                    sessionRunning = false;
                    break;
            }
        }
    }

    private static void handleRegister(Scanner input) {
        System.out.print("Username: ");
        String u = input.nextLine();
        System.out.print("Password: ");
        String p = input.nextLine();
        Farmer f = new Farmer(u, p);
        farmerDAO.save(f);
    }

    private static Farmer handleLogin(Scanner input) {
        System.out.print("Username: ");
        String u = input.nextLine();
        System.out.print("Password: ");
        String p = input.nextLine();
        return farmerDAO.login(u, p);
    }
}