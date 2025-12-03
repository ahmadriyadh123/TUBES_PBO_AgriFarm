package com.agrifarm.dao;

import com.agrifarm.model.Plant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlantDAO extends AbstractDAO<Plant> {

    @Override
    protected String getTableName() { return "plants"; }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO plants (name, type, growthStage, estimatedHarvestDate) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE plants SET name=?, type=?, growthStage=?, estimatedHarvestDate=? WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Plant plant) throws SQLException {
        ps.setString(1, plant.getName());
        ps.setString(2, plant.getType());
        ps.setString(3, plant.getGrowthStage());
        ps.setDate(4, Date.valueOf(plant.getEstimatedHarvestDate()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Plant plant) throws SQLException {
        setInsertParameters(ps, plant);
        ps.setInt(5, plant.getId());
    }

    @Override
    protected Plant mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Plant(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("type"),
            rs.getString("growthStage"),
            rs.getDate("estimatedHarvestDate").toLocalDate()
        );
    }
    
    // --- Spesifik ---
    // Overload save untuk relasi dengan Field jika diperlukan
    public void save(Plant plant, int fieldId) {
         String sql = "INSERT INTO plants (field_id, name, type, growthStage, estimatedHarvestDate) VALUES (?, ?, ?, ?, ?)";
         try(Connection conn = DatabaseConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
             ps.setInt(1, fieldId);
             ps.setString(2, plant.getName());
             ps.setString(3, plant.getType());
             ps.setString(4, plant.getGrowthStage());
             ps.setDate(5, Date.valueOf(plant.getEstimatedHarvestDate()));
             ps.executeUpdate();
         } catch(SQLException e) { e.printStackTrace(); }
    }

    public List<Plant> getByField(int fieldId) {
        List<Plant> list = new ArrayList<>();
        String sql = "SELECT * FROM plants WHERE field_id = ?"; // Pastikan kolom field_id ada di DB
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fieldId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) list.add(mapResultSetToEntity(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}