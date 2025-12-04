package com.agrifarm.model;

import com.agrifarm.farmer.IrrigationStrategy;
import java.util.ArrayList;
import java.util.List;

public class Farmer {

    private int id;
    private String name;
    private String password;
    private int age;
    private final List<Field> ownedFields;
    private String phone;
    private String address;

    private IrrigationStrategy irrigationStrategy;

    // Constructor untuk Registrasi (Tanpa ID)
    public Farmer(String name, String password) {
        this.name = name;
        this.password = password;
        this.ownedFields = new ArrayList<>();
        this.age = 30; 
        this.phone = "-";
        this.address = "Desa Agrifarm";
    }

    // Constructor dari Database (Dengan ID lengkap)
    public Farmer(int id, String name, int age, String phone, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.ownedFields = new ArrayList<>();
    }

    public int getId() { return id; }
    
    public String getName() { return name; }
    
    public String getPassword() { return password; }
    
    public int getAge() { return age; }
    
    public String getPhone() { return phone; }
    
    public String getAddress() { return address; }

    public List<Field> getOwnedFields() { return ownedFields; }

    public void assignField(Field field) {
        ownedFields.add(field);
    }

    // Validasi Login
    public boolean validatePassword(String inputPass) {
        if (this.password == null) return false;
        return this.password.equals(inputPass);
    }

    // Set Strategi Irigasi
    public void setStrategy(IrrigationStrategy irrigationStrategy) {
        this.irrigationStrategy = irrigationStrategy;
        System.out.println(">> Strategi irigasi diubah untuk petani " + this.name);
    }

    // Eksekusi irigasi menggunakan strategi yang dipilih
    public double executeIrrigation(FieldComponent component) {
        if (irrigationStrategy == null) {
            System.out.println(">> Peringatan: Strategi belum dipilih! Harap set strategi terlebih dahulu.");
            return 0.0;
        } else {
            // Polymorphism
            double waterUsed = component.irrigate(irrigationStrategy);

            if (waterUsed > 0) {
                System.out.println(">> [LAPORAN] Total air sesi ini: " + waterUsed + " Liter.");
            }
            return waterUsed;
        }
    }
    
    // Overload untuk Plant langsung
    public double executeIrrigation(Plant plant) {
        return executeIrrigation((FieldComponent) plant);
    }
}