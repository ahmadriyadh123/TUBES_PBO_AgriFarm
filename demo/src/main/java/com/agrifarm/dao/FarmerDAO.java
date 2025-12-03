package com.agrifarm.dao;

import com.agrifarm.model.Farmer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FarmerDAO {

    public void save(Farmer farmer) {
        String sql = "INSERT INTO farmers (name, age, phone, address) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, farmer.getName());
            ps.setInt(2, farmer.getAge());
            ps.setString(3, farmer.getPhone());
            ps.setString(4, farmer.getAddress());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Farmer> getAll() {
        List<Farmer> farmers = new ArrayList<>();
        String sql = "SELECT * FROM farmers";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Farmer farmer = new Farmer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("phone"),
                        rs.getString("address")
                );
                farmers.add(farmer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return farmers;
    }
}
