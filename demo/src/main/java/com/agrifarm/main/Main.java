package com.agrifarm.main;

import java.util.Scanner;
import java.util.List;
import java.util.Map; // [1] Import Map untuk statistik

import com.agrifarm.model.*;
import com.agrifarm.dao.*;
import com.agrifarm.farmer.*; // Import semua strategi (Flood, Sprinkler, Drip)
import com.agrifarm.service.IrrigationService; // Import Service

public class Main {

    // === 1. SETUP DAO & SERVICE ===
    private static FarmerDAO farmerDAO = new FarmerDAO();
    private static FieldDAO fieldDAO = new FieldDAO();
    
    // DAO ini disuntikkan ke dalam Service (Dependency Injection)
    private static PlantDAO plantDAO = new PlantDAO();       
    private static IrrigationLogDAO logDAO = new IrrigationLogDAO(); 

    // Inisialisasi Service dengan DAO yang dibutuhkan
    private static IrrigationService irrigationService = new IrrigationService(logDAO, plantDAO);

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean appRunning = true;

        while (appRunning) {
            System.out.println("\n=================================");
            System.out.println("   AGRI-FARM (SMART SYSTEM)");
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

    // === DASHBOARD UTAMA ===

    private static void runDashboard(Scanner input, Farmer pakTani) {
        boolean sessionRunning = true;

        while (sessionRunning) {
            // [2] Ambil Data Realtime dari Database
            Field myField = fieldDAO.getByFarmer(pakTani.getId());
            Plant currentPlant = null;
            
            if (myField != null) {
                // Mengambil tanaman pertama di lahan tersebut
                List<Plant> plants = plantDAO.getByField(myField.getId());
                if (!plants.isEmpty()) currentPlant = plants.get(0);
            }

            // Tampilan Status
            System.out.println("\n---------------------------------");
            System.out.println("DASHBOARD PETANI: " + pakTani.getName().toUpperCase());
            System.out.println("---------------------------------");
            System.out.println("Lahan        : " + (myField != null ? myField.getLocation() + " (" + myField.getSoilType() + ")" : "[BELUM ADA]"));
            System.out.println("Tanaman      : " + (currentPlant != null ? currentPlant.getName() + " - Fase " + currentPlant.getStage() : "[KOSONG]"));
            System.out.println("---------------------------------");
            System.out.println("1. Klaim Lahan Baru");
            System.out.println("2. Tanam Bibit");
            System.out.println("3. Cek Strategi Irigasi");
            System.out.println("4. Siram Tanaman");
            System.out.println("5. Lihat Riwayat Irigasi");
            System.out.println("6. Laporan Statistik Air");
            System.out.println("7. Logout");
            System.out.print("Pilih: ");

            String pilihan = input.nextLine();

            switch (pilihan) {
                case "1": // Buat Lahan
                    if (myField == null) {
                        System.out.print("Lokasi Lahan: ");
                        String loc = input.nextLine();
                        // Default Soil: Gembur
                        Field f = new Field(0, loc, 100, "Tanah Gembur", "Ready");
                        fieldDAO.save(f, pakTani.getId());
                        System.out.println(">> Lahan berhasil dibuat!");
                    } else {
                        System.out.println(">> Anda sudah memiliki lahan.");
                    }
                    break;

                case "2": // Tanam Bibit
                    if (myField != null) {
                        System.out.print("Nama Tanaman (cth: Padi, Jagung): ");
                        String pName = input.nextLine();
                        
                        // Default: Fase Bibit
                        Plant newPlant = new Plant(pName, "Bibit");
                        
                        // Simpan ke DB dengan relasi ke Field ID
                        plantDAO.save(newPlant, myField.getId());
                        System.out.println(">> " + pName + " berhasil ditanam.");
                    } else {
                        System.out.println(">> Harap klaim lahan terlebih dahulu (Menu 1).");
                    }
                    break;

                case "3": // [3] AUTO DETECT STRATEGY (Inti Strategy Pattern)
                    if (currentPlant == null) {
                        System.out.println(">> Tidak ada tanaman untuk dianalisis.");
                    } else {
                        System.out.println(">> [SISTEM] Menganalisis jenis tanaman & fase tumbuh...");
                        
                        String pName = currentPlant.getName().toLowerCase();
                        String pStage = currentPlant.getStage();

                        // Logika Pemilihan Strategi (Sesuai Slide Presentasi)
                        if (pName.contains("padi")) {
                            System.out.println(">> Deteksi: Tanaman Padi (Butuh genangan air).");
                            System.out.println(">> Strategi Terpilih: FLOOD IRRIGATION.");
                            pakTani.setStrategy(new FloodIrrigation());
                        } 
                        else if (pStage.equalsIgnoreCase("Bibit")) {
                            System.out.println(">> Deteksi: Fase Bibit (Butuh air halus/tetes).");
                            System.out.println(">> Strategi Terpilih: DRIP IRRIGATION.");
                            pakTani.setStrategy(new DripIrrigation());
                        } 
                        else {
                            System.out.println(">> Deteksi: Tanaman Umum/Lahan Luas.");
                            System.out.println(">> Strategi Terpilih: SPRINKLER IRRIGATION.");
                            pakTani.setStrategy(new SprinklerIrrigation());
                        }
                    }
                    break;

                case "4": // EKSEKUSI & SIMPAN LOG
                    if (currentPlant != null && myField != null) {
                        // 1. Jalankan irigasi (Polimorfisme Strategy)
                        double volume = pakTani.executeIrrigation(currentPlant);
                        
                        // 2. Jika ada air terpakai, simpan ke Database via Service
                        if (volume > 0) {
                            IrrigationLog log = new IrrigationLog(myField.getId(), volume);
                            
                            // Menggunakan service (Generic Service) untuk menyimpan
                            // Service otomatis menghandle validasi dasar
                            irrigationService.create(log); 
                            
                            System.out.println(">> [DB] Data penyiraman tersimpan.");
                        }
                    } else {
                        System.out.println(">> Pastikan Lahan & Tanaman tersedia, dan Strategi sudah dipilih (Menu 3).");
                    }
                    break;

                case "5": // LIHAT RIWAYAT (Sorted by Date - Collection Framework)
                    List<IrrigationLog> history = irrigationService.getSortedLogsByDate(pakTani.getId());
                    System.out.println("\n=== RIWAYAT IRIGASI (Terbaru) ===");
                    for (IrrigationLog log : history) {
                        System.out.println("- [" + log.getTimestamp() + "] Vol: " + log.getWaterVolume() + "L (Field " + log.getFieldId() + ")");
                    }
                    break;

                case "6": // [4] LAPORAN STATISTIK (HashMap - Collection Framework)
                    Map<Integer, Double> stats = irrigationService.getWaterUsageStats(pakTani.getId());
                    System.out.println("\n=== STATISTIK PENGGUNAAN AIR ===");
                    if (stats.isEmpty()) System.out.println("Belum ada data.");
                    
                    for (Map.Entry<Integer, Double> entry : stats.entrySet()) {
                        System.out.println(">> Lahan ID " + entry.getKey() + " : Total " + entry.getValue() + " Liter");
                    }
                    break;

                case "7":
                    sessionRunning = false;
                    break;
            }
        }
    }

    // === AUTH HELPER ===
    private static void handleRegister(Scanner input) {
        System.out.print("Username: "); String u = input.nextLine();
        System.out.print("Password: "); String p = input.nextLine();
        Farmer f = new Farmer(u, p);
        farmerDAO.save(f); // Menggunakan Generic DAO
    }

    private static Farmer handleLogin(Scanner input) {
        System.out.print("Username: "); String u = input.nextLine();
        System.out.print("Password: "); String p = input.nextLine();
        return farmerDAO.login(u, p);
    }
}