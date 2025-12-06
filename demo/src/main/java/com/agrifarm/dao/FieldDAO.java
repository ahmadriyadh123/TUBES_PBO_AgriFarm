package com.agrifarm.dao;

import com.agrifarm.model.Field;
import java.sql.*;
import java.util.logging.Level;

public class FieldDAO extends AbstractDAO<Field> {

    @Override
    protected String getTableName() {
        return "fields";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO fields (owner_id, location, size, soilType, status) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE fields SET owner_id=?, location=?, size=?, soilType=?, status=? WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Field field) throws SQLException {
        ps.setInt(1, 0);
        ps.setString(2, field.getLocation());
        ps.setDouble(3, field.getSize());
        ps.setString(4, field.getSoilType());
        ps.setString(5, field.getStatus());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Field field) throws SQLException {
        ps.setInt(1, field.getOwner() != null ? field.getOwner().getId() : 0);
        ps.setString(2, field.getLocation());
        ps.setDouble(3, field.getSize());
        ps.setString(4, field.getSoilType());
        ps.setString(5, field.getStatus());
        ps.setInt(6, field.getId());
    }

    @Override
    protected Field mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new Field(
                rs.getInt("id"),
                rs.getString("location"),
                rs.getDouble("size"),
                rs.getString("soilType"),
                rs.getString("status"));
    }

    // --- Method Spesifik ---

    public void save(Field field, int farmerId) {
        String sql = "INSERT INTO fields (owner_id, location, size, soilType, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, farmerId);
            ps.setString(2, field.getLocation());
            ps.setDouble(3, field.getSize());
            ps.setString(4, field.getSoilType());
            ps.setString(5, field.getStatus());
            ps.executeUpdate();
            logger.log(Level.INFO, ">> [DB] Field saved for Farmer ID: {0}", farmerId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Field getByFarmer(int farmerId) {
        // Fix: Avoid SELECT *
        String sql = "SELECT id, owner_id, location, size, soilType, status FROM fields WHERE owner_id = ? LIMIT 1";
        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, farmerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return mapResultSetToEntity(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Metode ini dipanggil oleh FarmManager untuk menetapkan lahan ke petani.
     */
    public void assignField(int farmerId, int fieldId) {
        String sql = "UPDATE fields SET owner_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, farmerId);
            ps.setInt(2, fieldId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                logger.log(Level.INFO, ">> [DB] Sukses assign Field ID {0} ke Farmer ID {1}",
                        new Object[] { fieldId, farmerId });
            } else {
                logger.warning(">> [DB] Gagal assign field (ID tidak ditemukan)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}