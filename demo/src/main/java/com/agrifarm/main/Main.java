package com.agrifarm.main;

import java.util.Scanner;
import com.agrifarm.model.Plant;
import com.agrifarm.farmer.Farmer;
import com.agrifarm.farmer.ManualIrrigation;
import com.agrifarm.farmer.AutomaticIrrigation;
import com.agrifarm.farmer.DripIrrigation;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== SISTEM MANAJEMEN PERTANIAN ===");
        System.out.print("Masukkan Nama Petani: ");
        String farmerName = input.nextLine();

        Farmer pakTani = new Farmer(farmerName, 5); 
        
        Plant currentPlant = null; 

        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n=================================");
            System.out.println("   MENU SIMULASI PERTANIAN");
            System.out.println("=================================");
            System.out.println("1. Tanam/Ganti Tanaman Baru");
            System.out.println("2. Ubah Strategi Irigasi");
            System.out.println("3. Lakukan Penyiraman (Irrigate)");
            System.out.println("4. Keluar Aplikasi");
            System.out.println("=================================");
            System.out.print("Pilih menu (1-4): ");

            int pilihan = 0;
            try {
                pilihan = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(">> Error: Masukkan angka saja!");
                continue;
            }

            switch (pilihan) {
                case 1:
                    System.out.print("\nMasukkan Nama Tanaman (cth: Padi): ");
                    String plantName = input.nextLine();
                    System.out.print("Masukkan Fase Tumbuh (cth: Bibit/Panen): ");
                    String plantStage = input.nextLine();
                    
                    currentPlant = new Plant(plantName, plantStage);
                    System.out.println(">> Berhasil menanam " + currentPlant.getName() + "!");
                    break;

                case 2:
                    System.out.println("\n--- Pilih Strategi Irigasi ---");
                    System.out.println("1. Manual (Ember/Selang)");
                    System.out.println("2. Otomatis (Sensor & Sprinkler)");
                    System.out.println("3. Drip (Tetes Hemat Air)");
                    System.out.print("Pilih Strategi (1-3): ");
                    
                    String stratChoice = input.nextLine();
                    
                    if (stratChoice.equals("1")) {
                        pakTani.setStrategy(new ManualIrrigation());
                    } else if (stratChoice.equals("2")) {
                        pakTani.setStrategy(new AutomaticIrrigation());
                    } else if (stratChoice.equals("3")) {
                        pakTani.setStrategy(new DripIrrigation());
                    } else {
                        System.out.println(">> Pilihan strategi tidak valid!");
                    }
                    break;

                case 3:
                    if (currentPlant == null) {
                        System.out.println("\n>> PERINGATAN: Anda belum menanam apapun! Pilih menu 1 dulu.");
                    } else {
                        System.out.println("\n>> Memulai proses irigasi...");
                        pakTani.performIrrigation(currentPlant); 
                    }
                    break;

                case 4:
                    System.out.println("Terima kasih telah menggunakan sistem ini. Sampai jumpa!");
                    isRunning = false;
                    break;

                default:
                    System.out.println(">> Menu tidak tersedia, silakan pilih lagi.");
            }
        }
        
        input.close();
    }
}