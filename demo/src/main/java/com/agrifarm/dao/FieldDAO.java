package com.agrifarm.dao;

import com.agrifarm.model.Field;
import java.sql.*;

public class FieldDAO extends AbstractDAO<Field> {

    @Override
    protected String getTableName() { return "fields"; }

    @Override
    protected String getInsertSql() {
        // Catatan: Asumsi owner_id diset manual atau via logic lain, disini kita pakai default 0 jika null
        return "INSERT INTO fields (owner_id, location, size, soilType, status) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE fields SET owner_id=?, location=?, size=?, soilType=?, status=? WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Field field) throws SQLException {
        // Karena di Field.java field owner itu Object Farmer, kita butuh ID-nya.
        // Jika logic Anda memisahkan ID, sesuaikan disini. 
        // Untuk contoh ini kita ambil asumsi owner_id dihandle terpisah atau field punya properti ownerId.
        // Sesuai kode lama: fieldDAO.save(field, farmerId). 
        // KITA PERLU OVERLOAD SAVE DI BAWAH UNTUK KASUS KHUSUS INI atau sesuaikan model.
        
        // Agar generic tetap jalan, kita isi default owner_id = 0 (belum ada pemilik)
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
            rs.getString("status")
        );
    }
    
    // --- Method Spesifik ---
    
    // Kita Overload save karena butuh farmerId
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
            System.out.println(">> [DB] Field saved for Farmer ID: " + farmerId);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public Field getByFarmer(int farmerId) {
        String sql = "SELECT * FROM fields WHERE owner_id = ? LIMIT 1";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, farmerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSetToEntity(rs);
        } catch (Exception e) { e.printStackTrace(); }
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
                System.out.println(">> [DB] Sukses assign Field ID " + fieldId + " ke Farmer ID " + farmerId);
            } else {
                System.out.println(">> [DB] Gagal assign field (ID tidak ditemukan)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}