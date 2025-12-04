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

    public Farmer(String name, String password) {
        this.name = name;
        this.password = password;
        this.ownedFields = new ArrayList<>();
        this.age = 30; 
        this.phone = "-";
        this.address = "Desa Agrifarm";
    }

    public Farmer(int id, String name, int age, String phone, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.ownedFields = new ArrayList<>();
    }

    public boolean validatePassword(String inputPass) {
        return this.password.equals(inputPass);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public int getAge() { return age; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    public List<Field> getOwnedFields() {
        return ownedFields;
    }

    public void assignField(Field field) {
        ownedFields.add(field);
    }

    public void setStrategy(IrrigationStrategy irrigationStrategy) {
        this.irrigationStrategy = irrigationStrategy;
        System.out.println(">> Strategi irigasi diubah untuk petani " + this.name);
    }

    /**
     * Menjalankan irigasi sesuai strategi yang dipilih.
     * Metode ini dipanggil di Main.java case 3.
     */
    public double executeIrrigation(Plant plant) {
        if (irrigationStrategy == null) {
            System.out.println(">> Peringatan: Strategi belum dipilih! Harap set strategi terlebih dahulu.");
            return 0.0;
        } else {
            double waterUsed = irrigationStrategy.irrigate(plant);
            
            if (waterUsed > 0) {
                System.out.println(">> [LAPORAN] Total air: " + waterUsed + " Liter.");
            }
            return waterUsed;
        }
    }
}
