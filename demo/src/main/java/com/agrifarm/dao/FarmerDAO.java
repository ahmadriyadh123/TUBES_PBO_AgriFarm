package com.agrifarm.dao;

import com.agrifarm.model.Farmer;
import java.sql.*;

public class FarmerDAO extends AbstractDAO<Farmer> {
    @Override
    protected String getTableName() { return "farmers"; }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO farmers (name, password, age, phone, address) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE farmers SET name=?, password=?, age=?, phone=?, address=? WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Farmer farmer) throws SQLException {
        ps.setString(1, farmer.getName());
        ps.setString(2, farmer.getPassword());
        ps.setInt(3, farmer.getAge());
        ps.setString(4, farmer.getPhone());
        ps.setString(5, farmer.getAddress());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Farmer farmer) throws SQLException {
        setInsertParameters(ps, farmer);
        ps.setInt(6, farmer.getId());
    }

    @Override
    protected Farmer mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Farmer(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("age"),
            rs.getString("phone"),
            rs.getString("address")
        );
    }

    
    public Farmer login(String username, String password) {
        String sql = "SELECT * FROM farmers WHERE name = ? AND password = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}