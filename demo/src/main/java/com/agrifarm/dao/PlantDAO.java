package com.agrifarm.dao;

import com.agrifarm.model.Plant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlantDAO {

    public void save(Plant plant) {
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
        List<Plant> plants = new ArrayList<>();
        String sql = "SELECT * FROM plants";

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
