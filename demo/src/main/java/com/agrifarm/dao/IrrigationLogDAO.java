package com.agrifarm.dao;

import com.agrifarm.model.IrrigationLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IrrigationLogDAO {

    public void save(IrrigationLog log) {
        String sql = "INSERT INTO irrigation_logs (fieldId, waterVolume) VALUES (?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, log.getFieldId());
            ps.setDouble(2, log.getWaterVolume());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<IrrigationLog> getAll() {
        List<IrrigationLog> logs = new ArrayList<>();
        String sql = "SELECT * FROM irrigation_logs";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                IrrigationLog log = new IrrigationLog(
                        rs.getInt("id"),
                        rs.getInt("fieldId"),
                        rs.getDouble("waterVolume"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                );
                logs.add(log);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return logs;
    }

    public List<IrrigationLog> getByFarmer(int farmerId) {
        // Query ini mengasumsikan kita mencari log berdasarkan lahan milik petani tersebut
        // Kita perlu JOIN antara irrigation_logs -> fields -> farmers
        String sql = "SELECT l.* FROM irrigation_logs l " +
                     "JOIN fields f ON l.fieldId = f.id " +
                     "WHERE f.owner_id = " + farmerId;
        return fetchLogs(sql);
    }

    private List<IrrigationLog> fetchLogs(String sql) {
        List<IrrigationLog> logs = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                IrrigationLog log = new IrrigationLog(
                        rs.getInt("id"),
                        rs.getInt("fieldId"),
                        rs.getDouble("waterVolume"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                );
                logs.add(log);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logs;
    }
}
