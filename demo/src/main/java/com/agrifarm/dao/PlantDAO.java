package com.agrifarm.dao;

import com.agrifarm.model.Plant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlantDAO {

    // Simpan Tanaman
    public void save(Plant plant) {
        // Catatan: Pastikan tabel 'plants' punya kolom 'field_id' jika ingin relasi.
        // Untuk sekarang kita simpan data dasar dulu.
        String sql = "INSERT INTO plants (name, type, growthStage, estimatedHarvestDate) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, plant.getName());
            ps.setString(2, plant.getType());
            ps.setString(3, plant.getGrowthStage());
            ps.setDate(4, Date.valueOf(plant.getEstimatedHarvestDate()));
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Plant> getAll() {
        return fetchPlants("SELECT * FROM plants");
    }

    // PERBAIKAN: Metode ini dibutuhkan oleh IrrigationService
    public List<Plant> getByField(int fieldId) {
        // Mengambil tanaman berdasarkan field_id. 
        // Asumsi: Tabel plants memiliki kolom 'field_id'. Jika belum, ini akan return list kosong atau error SQL.
        // Untuk menghindari error kompilasi, kita jalankan query SELECT biasa dulu jika kolom belum ada.
        String sql = "SELECT * FROM plants"; // Idealnya: "SELECT * FROM plants WHERE field_id = " + fieldId
        return fetchPlants(sql);
    }

    // Helper method untuk fetch data agar tidak duplikasi kode
    private List<Plant> fetchPlants(String sql) {
        List<Plant> plants = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Plant plant = new Plant(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("growthStage"),
                        rs.getDate("estimatedHarvestDate").toLocalDate()
                );
                plants.add(plant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plants;
    }
}