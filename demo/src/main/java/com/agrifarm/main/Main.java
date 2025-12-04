package com.agrifarm.main;

import java.util.Map;
import java.util.Scanner;
import com.agrifarm.model.*;
import com.agrifarm.dao.FarmerDAO;
import com.agrifarm.model.*;
import com.agrifarm.dao.FieldDAO;
import com.agrifarm.dao.IrrigationLogDAO;
import com.agrifarm.dao.PlantDAO;
import com.agrifarm.farmer.*;
import com.agrifarm.service.IrrigationService;

public class Main {

    // DAO untuk akses Database
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
            System.out.println("===========   AGRI-FARM ===========");
            System.out.println("=================================");
            System.out.println("1. Login");
            System.out.println("2. Registrasi Akun Baru");
            System.out.println("3. Keluar");
            System.out.print("Pilih menu: ");
            
            String menuAwal = input.nextLine();

            if (menuAwal.equals("1")) {
                // --- PROSES LOGIN DB ---
                Farmer loggedInFarmer = handleLogin(input);
                if (loggedInFarmer != null) {
                    runDashboard(input, loggedInFarmer);
                }

            } else if (menuAwal.equals("2")) {
                // --- PROSES REGISTRASI DB ---
                handleRegister(input);

            } else if (menuAwal.equals("3")) {
                System.out.println("Sampai jumpa!");
                appRunning = false;
            }
        }
        input.close();
    }

    // === METHOD AUTH (DATABASE) ===
    
    private static void handleRegister(Scanner input) {
        System.out.print("Username Baru : ");
        String name = input.nextLine();
        System.out.print("Password Baru : ");
        String pass = input.nextLine();

        // Simpan ke Database via DAO
        Farmer newFarmer = new Farmer(name, pass);
        farmerDAO.save(newFarmer);
    }

    private static Farmer handleLogin(Scanner input) {
        System.out.print("Username : ");
        String name = input.nextLine();
        System.out.print("Password : ");
        String pass = input.nextLine();

        // Cek ke Database
        Farmer farmer = farmerDAO.login(name, pass);
        
        if (farmer != null) {
            System.out.println(">> Login Sukses! ID: " + farmer.getId());
            return farmer;
        } else {
            System.out.println(">> Login Gagal! Cek username/password.");
            return null;
        }
    }

    // === DASHBOARD UTAMA ===

    private static void runDashboard(Scanner input, Farmer pakTani) {
        boolean sessionRunning = true;
        Plant currentPlant = null;

        while (sessionRunning) {
            Field myField = fieldDAO.getByFarmer(pakTani.getId());
            String fieldStatus = (myField == null) ? "[BELUM ADA]" : myField.getSoilType();

            String plantStatus = "[KOSONG]";
            if (currentPlant != null) {
                String bar = "[";
                int p = currentPlant.getProgress() / 10; // Skala 10 bar
                for (int i=0; i<10; i++) {
                    bar += (i < p) ? "█" : "-";
                }
                bar += "] " + currentPlant.getProgress() + "%";
                
                if (currentPlant.isHarvestReady()) {
                    plantStatus = currentPlant.getName() + " " + bar + " [SIAP PANEN!]";
                } else {
                    plantStatus = currentPlant.getName() + " " + bar;
                }
            }

            System.out.println("Status Tanaman : " + plantStatus);

            System.out.println("\n---------------------------------");
            System.out.println("      DASHBOARD: " + pakTani.getName().toUpperCase());
            System.out.println("---------------------------------");
            System.out.println("Tanah Lahan    : " + fieldStatus);
            System.out.println("Status Tanaman : " + (currentPlant == null ? "[KOSONG]" : currentPlant.getName()));
            System.out.println("---------------------------------");
            System.out.println("1. Cek Lahan");
            System.out.println("2. Tanam Bibit");
            System.out.println("3. Tentukan Strategi Irigasi");
            System.out.println("4. Jalankan Irigasi");
            System.out.println("5. Statistik Penggunaan Air");
            System.out.println("6. Logout");
            System.out.print("Pilih: ");

            String pilihan = input.nextLine();

            // Validasi: Harus punya lahan di database
            if ((pilihan.equals("2") || pilihan.equals("3") || pilihan.equals("4")) && myField == null) {
                System.out.println("\n>> [DITOLAK] Anda belum punya lahan di database!");
                System.out.println(">> Pilih Menu 1 untuk klaim lahan.");
                continue;
            }

            switch (pilihan) {
                case "1":
                    if (myField != null) {
                        System.out.println(">> Anda sudah punya lahan: " + myField.getSoilType());
                    } else {
                        System.out.println("Pilih Jenis Tanah: 1.Liat 2.Gembur 3.Pasir");
                        String t = input.nextLine();
                        String type = "Tanah Gembur";
                        if (t.equals("1")) type = "Tanah Liat";
                        if (t.equals("3")) type = "Tanah Pasir";

                        // Simpan Lahan ke Database
                        Field f = new Field("Lokasi Utama", 100); // Default
                        // Kita butuh setter/metode untuk set soilType di objek Field jika belum ada
                        // Asumsi konstruktor Field sudah mendukung atau pakai setter manual di logic ini
                        // Untuk simpelnya, kita anggap FieldDAO menangani string soilType yang kita kirim
                        // *Modifikasi sedikit FieldDAO di atas untuk menerima objek Field yang sudah diset soilType-nya*
                        // Di sini kita buat objek Field sementara yang membawa data soilType
                        Field newField = new Field(0, "Lokasi Utama", 100, type, "Available"); 
                        
                        fieldDAO.save(newField, pakTani.getId());
                        System.out.println(">> Lahan berhasil disimpan ke Database!");
                    }
                    break;

                case "2":
                    System.out.print("Nama Tanaman: ");
                    String pName = input.nextLine();
                    Plant pot = new Plant(pName, "Bibit");
                    
                    if (pot.isCompatibleWithSoil(myField.getSoilType())) {
                        currentPlant = pot;
                        System.out.println(">> Berhasil menanam " + pName);
                    } else {
                        System.out.println(">> Gagal! Tanah tidak cocok. Butuh: " + pot.getRequiredSoilType());
                    }
                    break;

                case "3":
                    if (currentPlant == null) {
                        System.out.println(">> Belum ada tanaman. Tidak bisa menentukan strategi.");
                    } else {
                        System.out.println("\n>> [SYSTEM] Menganalisis kondisi lahan & tanaman...");
                        
                        String name = currentPlant.getName().toLowerCase();
                        String stage = currentPlant.getStage();

                        if (name.contains("padi")) {
                            System.out.println(">> Kondisi: Tanaman Padi terdeteksi.");
                            System.out.println(">> REKOMENDASI: Flood Irrigation Strategy.");
                            pakTani.setStrategy(new FloodIrrigation());
                        } 
                        else if (stage.equalsIgnoreCase("Bibit")) {
                            System.out.println(">> Kondisi: Fase Bibit (Rentan).");
                            System.out.println(">> REKOMENDASI: Drip Irrigation Strategy.");
                            pakTani.setStrategy(new DripIrrigation());
                        } 
                        else {
                            System.out.println(">> Kondisi: Tanaman Dewasa / Lahan Luas.");
                            System.out.println(">> REKOMENDASI: Sprinkler Irrigation Strategy.");
                            pakTani.setStrategy(new SprinklerIrrigation());
                        }
                    }
                    break;

                case "4":
                    if(currentPlant != null) pakTani.executeIrrigation(currentPlant);
                    else System.out.println(">> Tanaman kosong.");
                    break;
                    
                case "5":
                    Map<Integer, Double> stats = irrigationService.getWaterUsageStats(pakTani.getId());
                    System.out.println(">> Statistik Air per Lahan:");
                    for (Map.Entry<Integer, Double> entry : stats.entrySet()) {
                        System.out.println("   - Field ID " + entry.getKey() + ": " + entry.getValue() + " Liter");
                    }
                    break;

                case "6":
                    sessionRunning = false;
                    break;

            }
        }
    }
}