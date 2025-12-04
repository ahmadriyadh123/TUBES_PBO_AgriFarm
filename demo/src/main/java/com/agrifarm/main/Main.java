package com.agrifarm.main;

import java.util.Scanner;
import java.util.List;
import java.util.Map; // Import Map untuk statistik

import com.agrifarm.model.*;
import com.agrifarm.dao.*;
import com.agrifarm.farmer.*;
import com.agrifarm.service.IrrigationService;

public class Main {

    // ==========================================
    // 1. SETUP DAO & SERVICE (DEPENDENCY INJECTION)
    // ==========================================
    
    // DAO Standar
    private static FarmerDAO farmerDAO = new FarmerDAO();
    private static FieldDAO fieldDAO = new FieldDAO();
    
    // DAO yang disuntikkan ke dalam Service
    private static PlantDAO plantDAO = new PlantDAO();       
    private static IrrigationLogDAO logDAO = new IrrigationLogDAO(); 

    // Inisialisasi Service dengan DAO yang dibutuhkan
    private static IrrigationService irrigationService = new IrrigationService(logDAO, plantDAO);

    // ==========================================
    // 2. MAIN METHOD (ENTRY POINT)
    // ==========================================
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

    // ==========================================
    // 3. DASHBOARD UTAMA (LOGIKA BISNIS)
    // ==========================================
    private static void runDashboard(Scanner input, Farmer pakTani) {
        boolean sessionRunning = true;
        
        // Variabel untuk menyimpan tanaman yang SEDANG DIPILIH user untuk aksi
        Plant selectedTargetPlant = null;

        while (sessionRunning) {
            // [A] Ambil Data Lahan dari Database
            Field myField = fieldDAO.getByFarmer(pakTani.getId());
            
            // [B] Ambil SEMUA Tanaman di lahan tersebut (List)
            List<Plant> allPlants = null;
            if (myField != null) {
                allPlants = plantDAO.getByField(myField.getId());
            }

            // --- TAMPILAN STATUS DASHBOARD ---
            System.out.println("\n---------------------------------");
            System.out.println("DASHBOARD PETANI: " + pakTani.getName().toUpperCase());
            System.out.println("---------------------------------");
            System.out.println("Lahan        : " + (myField != null ? myField.getLocation() + " (" + myField.getSoilType() + ")" : "[BELUM ADA]"));

            if (allPlants != null && !allPlants.isEmpty()) {
                System.out.println("Total Tanaman: " + allPlants.size() + " Jenis");
                System.out.print("Daftar       : ");
                for(Plant p : allPlants) System.out.print("[" + p.getName() + "] ");
                System.out.println();
            } else {
                System.out.println("Tanaman      : [KOSONG]");
            }
            
            // Info Target Aktif (Yang akan disiram)
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
            System.out.println("7. Logout");
            System.out.print("Pilih: ");

            String pilihan = input.nextLine();

            switch (pilihan) {
                case "1": // FITUR: KLAIM LAHAN DENGAN PILIHAN TANAH
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

                        String soilType = "Tanah Gembur"; // Default

                        switch (soilChoice) {
                            case "1": soilType = "Tanah Liat"; break;
                            case "2": soilType = "Tanah Pasir"; break;
                            case "3": soilType = "Tanah Gembur"; break;
                            case "4": soilType = "Tanah Lempung"; break;
                            case "5": soilType = "Tanah Gambut"; break;
                            default: 
                                System.out.println(">> Pilihan tidak valid! Menggunakan default (Gembur).");
                                break;
                        }

                        // Membuat objek Field
                        Field f = new Field(0, loc, 100, soilType, "Ready");
                        
                        fieldDAO.save(f, pakTani.getId());
                        System.out.println(">> Lahan di " + loc + " dengan jenis " + soilType + " berhasil disimpan!");
                        
                    } else {
                        System.out.println(">> Anda sudah memiliki lahan (" + myField.getSoilType() + ").");
                    }
                    break;

                case "2": // FITUR: TANAM BIBIT (MULTI-PLANT)
                    if (myField != null) {
                        System.out.print("Nama Tanaman (cth: Padi, Jagung, Cabai): ");
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

                case "3": // FITUR: PILIH TANAMAN & AUTO-DETECT STRATEGY
                    if (allPlants == null || allPlants.isEmpty()) {
                        System.out.println(">> Tidak ada tanaman di lahan ini.");
                    } else {
                        System.out.println("\n--- PILIH TANAMAN UNTUK IRIGASI ---");
                        // 1. Tampilkan List Tanaman dengan Nomor
                        for (int i = 0; i < allPlants.size(); i++) {
                            Plant p = allPlants.get(i);
                            System.out.println((i + 1) + ". " + p.getName() + " (Fase: " + p.getStage() + ")");
                        }
                        
                        // 2. User Memilih
                        System.out.print("Pilih Nomor Tanaman: ");
                        try {
                            int idx = Integer.parseInt(input.nextLine()) - 1;

                            if (idx >= 0 && idx < allPlants.size()) {
                                // Set Target Tanaman ke Variabel
                                selectedTargetPlant = allPlants.get(idx);
                                
                                System.out.println("\n>> [ANALISIS] Menganalisis " + selectedTargetPlant.getName() + "...");
                                
                                // 3. Logika Penentuan Strategi KHUSUS tanaman ini
                                String pName = selectedTargetPlant.getName().toLowerCase();
                                String pStage = selectedTargetPlant.getStage();

                                if (pName.contains("padi")) {
                                    System.out.println(">> Tipe: Padi -> Butuh Flood Irrigation.");
                                    pakTani.setStrategy(new FloodIrrigation());
                                } 
                                else if (pStage.equalsIgnoreCase("Bibit")) {
                                    System.out.println(">> Tipe: Bibit -> Butuh Drip Irrigation (Tetes).");
                                    pakTani.setStrategy(new DripIrrigation());
                                } 
                                else {
                                    System.out.println(">> Tipe: Umum -> Menggunakan Sprinkler.");
                                    pakTani.setStrategy(new SprinklerIrrigation());
                                }
                                System.out.println(">> SIAP MENYIRAM: " + selectedTargetPlant.getName());

                            } else {
                                System.out.println(">> Pilihan salah.");
                                selectedTargetPlant = null; // Reset
                            }
                        } catch (Exception e) {
                            System.out.println(">> Input harus angka.");
                        }
                    }
                    break;

                case "4": // FITUR: EKSEKUSI & SIMPAN LOG
                    if (selectedTargetPlant != null && myField != null) {
                        System.out.println("\n>> [EKSEKUSI] Menyiram tanaman: " + selectedTargetPlant.getName());
                        
                        // Jalankan strategi yang sudah di-set di Menu 3
                        double volume = pakTani.executeIrrigation(selectedTargetPlant);
                        
                        if (volume > 0) {
                            // Simpan log ke DB via Service
                            IrrigationLog log = new IrrigationLog(myField.getId(), volume);
                            irrigationService.create(log);
                            System.out.println(">> Data tersimpan untuk " + selectedTargetPlant.getName());
                        }
                    } else {
                        System.out.println(">> [ERROR] Belum ada tanaman yang dipilih!");
                        System.out.println(">> Silakan pilih menu nomor 3 terlebih dahulu.");
                    }
                    break;

                case "5": // FITUR: LIHAT RIWAYAT (Sorted by Date - Collection Framework)
                    List<IrrigationLog> history = irrigationService.getSortedLogsByDate(pakTani.getId());
                    System.out.println("\n=== RIWAYAT IRIGASI (Terbaru) ===");
                    if (history.isEmpty()) System.out.println("Belum ada data.");
                    
                    for (IrrigationLog log : history) {
                        System.out.println("- [" + log.getTimestamp() + "] Vol: " + log.getWaterVolume() + "L (Field " + log.getFieldId() + ")");
                    }
                    break;

                case "6": // FITUR: LAPORAN STATISTIK (HashMap - Collection Framework)
                    Map<Integer, Double> stats = irrigationService.getWaterUsageStats(pakTani.getId());
                    System.out.println("\n=== STATISTIK PENGGUNAAN AIR ===");
                    if (stats.isEmpty()) System.out.println("Belum ada data.");
                    
                    for (Map.Entry<Integer, Double> entry : stats.entrySet()) {
                        System.out.println(">> Lahan ID " + entry.getKey() + " : Total " + entry.getValue() + " Liter");
                    }
                    break;

                case "7": // FITUR BARU: CEK ESTIMASI PANEN (COMPOSITE PATTERN)
                    if (myField != null) {
                        // 1. Reset komponen lahan (agar tidak duplikat saat loop)
                        // Karena kita mengambil data fresh dari DB, kita rakit ulang struktur Composite-nya
                        // (Cara manual karena FieldDAO mengembalikan objek Field polos)
                        
                        Field compositeField = myField; // Ini adalah Root Composite
                        
                        // 2. Ambil tanaman dari DB
                        List<Plant> dbPlants = plantDAO.getByField(compositeField.getId());
                        
                        // 3. RAKIT STRUKTUR COMPOSITE (Masukkan Leaf ke Composite)
                        // Kita masukkan objek Plant (Leaf) ke dalam objek Field (Composite)
                        // Catatan: Idealnya ini dilakukan di Service, tapi untuk demo kita taruh di Main.
                        for (Plant p : dbPlants) {
                            compositeField.addComponent(p);
                        }

                        System.out.println("\n=== LAPORAN ESTIMASI PANEN (COMPOSITE PATTERN) ===");
                        
                        // 4. PANGGIL METHOD COMPOSITE
                        // displayInfo() akan otomatis memanggil displayInfo() milik semua anak
                        compositeField.displayInfo(); 

                        System.out.println("---------------------------------------------");
                        
                        // calculateYield() akan menjumlahkan yield semua anak secara rekursif
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

    // ==========================================
    // 4. AUTH HELPER METHODS
    // ==========================================
    
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