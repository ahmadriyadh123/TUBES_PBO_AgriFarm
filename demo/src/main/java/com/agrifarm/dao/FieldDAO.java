package com.agrifarm.dao;

import com.agrifarm.model.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FieldDAO {

    public void save(Field field) {
        String sql = "INSERT INTO fields (location, size, soilType, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, field.getLocation());
            ps.setDouble(2, field.getSize());
            ps.setString(3, field.getSoilType());
            ps.setString(4, field.getStatus());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void assignField(int farmerId, int fieldId) {
        String sql = "UPDATE fields SET owner_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, farmerId);
            ps.setInt(2, fieldId);
            int rows = ps.executeUpdate();
            
            if (rows > 0) {
                System.out.println("✔ Sukses assign Field ID " + fieldId + " ke Farmer ID " + farmerId);
            } else {
                System.out.println("⚠ Gagal assign field (ID tidak ditemukan)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Field> getAll() {
        List<Field> fields = new ArrayList<>();
        String sql = "SELECT * FROM fields";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Field field = new Field(
                        rs.getInt("id"),
                        rs.getString("location"),
                        rs.getDouble("size"),
                        rs.getString("soilType"),
                        rs.getString("status")
                );
                fields.add(field);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fields;
    }

    public List<Field> getByFarmer(int farmerId) {
        List<Field> fields = new ArrayList<>();
        String sql = "SELECT * FROM fields WHERE owner_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, farmerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                     Field field = new Field(
                        rs.getInt("id"),
                        rs.getString("location"),
                        rs.getDouble("size"),
                        rs.getString("soilType"),
                        rs.getString("status")
                    );
                    fields.add(field);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fields;
    }
}
