package com.agrifarm.model;

import com.agrifarm.farmer.IrrigationStrategy;
import java.util.ArrayList;
import java.util.List;


public class Farmer {

    private int id;
    private String name;
    private int age;
    private final List<Field> ownedFields;
    private String phone;
    private String address;

    private IrrigationStrategy irrigationStrategy;

    public Farmer(String name) {
        this.name = name;
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

    public int getId() { return id; }
    public String getName() { return name; }
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
    public void performIrrigation(Plant plant) {
        if (irrigationStrategy == null) {
            System.out.println(">> Peringatan: Anda belum memilih strategi irigasi!");
        } else {
            irrigationStrategy.irrigate(plant);
        }
    }
}
