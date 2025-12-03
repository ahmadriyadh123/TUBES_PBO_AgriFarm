package com.agrifarm.main;

import java.util.Scanner;
import com.agrifarm.model.Plant;
import com.agrifarm.model.Farmer;
import com.agrifarm.farmer.ManualIrrigation;
import com.agrifarm.farmer.AutomaticIrrigation;
import com.agrifarm.farmer.DripIrrigation;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        // Inisialisasi awal: Petani & Tanaman belum ada (null)
        Farmer pakTani = null;
        Plant currentPlant = null;
        String currentFieldSoil = "-"; // Menandakan belum ada lahan terdaftar

        System.out.println("=== SISTEM MANAJEMEN PERTANIAN ===");
        
        boolean isRunning = true;

        while (isRunning) {
            // TAMPILAN STATUS DASHBOARD
            String statusPetani = (pakTani == null) ? "[BELUM REGISTRASI]" : pakTani.getName();
            String statusLahan  = (currentFieldSoil.equals("-")) ? "[BELUM DISET]" : currentFieldSoil;
            String statusTanaman = (currentPlant == null) ? "[KOSONG]" : currentPlant.getName();

            System.out.println("\n=================================");
            System.out.println("   MENU SIMULASI PERTANIAN");
            System.out.println("=================================");
            System.out.println("Petani  : " + statusPetani);
            System.out.println("Lahan   : " + statusLahan);
            System.out.println("Tanaman : " + statusTanaman);
            System.out.println("---------------------------------");
            System.out.println("1. Registrasi Petani & Data Lahan (WAJIB PERTAMA)");
            System.out.println("2. Tanam/Ganti Tanaman");
            System.out.println("3. Ubah Strategi Irigasi");
            System.out.println("4. Lakukan Penyiraman");
            System.out.println("5. Keluar Aplikasi");
            System.out.println("=================================");
            System.out.print("Pilih menu (1-5): ");

            int pilihan = 0;
            try {
                String val = input.nextLine();
                if (val.isEmpty()) continue;
                pilihan = Integer.parseInt(val);
            } catch (NumberFormatException e) {
                System.out.println(">> Error: Angka tidak valid!");
                continue;
            }

            // === FITUR KEAMANAN: Validasi Akses Menu ===
            // Jika memilih menu 2, 3, atau 4 TAPI belum registrasi (pakTani null), tolak akses.
            if ((pilihan >= 2 && pilihan <= 4)) {
                if (pakTani == null || currentFieldSoil.equals("-")) {
                    System.out.println("\n[AKSES DITOLAK] ⚠");
                    System.out.println("Anda belum mendaftarkan Petani dan Lahan.");
                    System.out.println("Silakan pilih Menu 1 terlebih dahulu!");
                    continue; // Kembali ke awal loop
                }
            }

            switch (pilihan) {
                case 1: // REGISTRASI
                    System.out.println("\n--- REGISTRASI DATA ---");
                    
                    // 1. Input Nama Petani
                    System.out.print("Masukkan Nama Petani: ");
                    String nameInput = input.nextLine();
                    pakTani = new Farmer(nameInput); // Objek Farmer dibuat di sini
                    
                    // 2. Input Data Lahan
                    System.out.println("\nPilih Jenis Tanah Lahan yang Anda miliki:");
                    System.out.println("- Tanah Liat");
                    System.out.println("- Tanah Gembur");
                    System.out.println("- Tanah Pasir");
                    System.out.print("Input Jenis Tanah: ");
                    String soilInput = input.nextLine();
                    
                    if (soilInput.trim().isEmpty()) {
                        currentFieldSoil = "Tanah Gembur"; // Default jika kosong
                    } else {
                        currentFieldSoil = soilInput;
                    }

                    System.out.println(">> Registrasi Berhasil!");
                    System.out.println(">> Selamat datang, " + pakTani.getName() + ".");
                    System.out.println(">> Lahan tipe '" + currentFieldSoil + "' telah siap.");
                    break;

                case 2: // TANAM (Menggunakan data tanah dari Menu 1)
                    System.out.println("\n--- PENANAMAN ---");
                    System.out.print("Nama Tanaman (cth: Padi, Jagung): ");
                    String plantName = input.nextLine();
                    System.out.print("Fase Tumbuh (cth: Bibit): ");
                    String plantStage = input.nextLine();
                    
                    Plant potentialPlant = new Plant(plantName, plantStage);
                    String requiredSoil = potentialPlant.getRequiredSoilType(); // Mengambil syarat tanah (fitur sebelumnya)
                    
                    System.out.println(">> Info Biologis: " + plantName + " membutuhkan " + requiredSoil);
                    
                    // Cek kecocokan dengan lahan yang SUDAH didaftarkan di Menu 1
                    if (potentialPlant.isCompatibleWithSoil(currentFieldSoil)) {
                        currentPlant = potentialPlant;
                        System.out.println("\n[BERHASIL] Tanaman cocok dengan lahan Anda!");
                        System.out.println(plantName + " berhasil ditanam.");
                    } else {
                        System.out.println("\n[GAGAL] Peringatan Ekologis!");
                        System.out.println("Lahan Anda (" + currentFieldSoil + ") TIDAK COCOK untuk " + plantName + ".");
                        System.out.println("Disarankan menanam tanaman yang cocok atau mengganti lahan.");
                    }
                    break;

                case 3: // STRATEGI
                    System.out.println("\n--- UBAH STRATEGI IRIGASI ---");
                    System.out.println("1. Manual (Ember)");
                    System.out.println("2. Otomatis (Sprinkler)");
                    System.out.println("3. Drip (Tetes)");
                    System.out.print("Pilih (1-3): ");
                    String s = input.nextLine();
                    
                    if (s.equals("1")) pakTani.setStrategy(new ManualIrrigation());
                    else if (s.equals("2")) pakTani.setStrategy(new AutomaticIrrigation());
                    else if (s.equals("3")) pakTani.setStrategy(new DripIrrigation());
                    else System.out.println(">> Pilihan tidak valid.");
                    break;

                case 4: // SIRAM
                    if (currentPlant == null) {
                        System.out.println("\n>> Lahan masih kosong! Silakan tanam sesuatu di Menu 2.");
                    } else {
                        System.out.println("\n>> Memproses irigasi pada lahan " + currentFieldSoil + "...");
                        pakTani.performIrrigation(currentPlant);
                    }
                    break;

                case 5:
                    System.out.println("Terima kasih. Sampai jumpa!");
                    isRunning = false;
                    break;

                default:
                    System.out.println("Menu tidak tersedia.");
            }
        }
        input.close();
    }
}