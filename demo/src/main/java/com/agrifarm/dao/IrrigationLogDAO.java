package com.agrifarm.dao;

import com.agrifarm.model.IrrigationLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IrrigationLogDAO extends AbstractDAO<IrrigationLog> {

    @Override
    protected String getTableName() { return "irrigation_logs"; }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO irrigation_logs (fieldId, waterVolume, timestamp) VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE irrigation_logs SET fieldId=?, waterVolume=?, timestamp=? WHERE id=?";
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, IrrigationLog log) throws SQLException {
        ps.setInt(1, log.getFieldId());
        ps.setDouble(2, log.getWaterVolume());
        ps.setTimestamp(3, Timestamp.valueOf(log.getTimestamp()));
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, IrrigationLog log) throws SQLException {
        setInsertParameters(ps, log);
        ps.setInt(4, log.getId());
    }

    @Override
    protected IrrigationLog mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new IrrigationLog(
            rs.getInt("id"),
            rs.getInt("fieldId"),
            rs.getDouble("waterVolume"),
            rs.getTimestamp("timestamp").toLocalDateTime()
        );
    }
    
    public List<IrrigationLog> getByFarmer(int farmerId) {
        String sql = "SELECT l.* FROM irrigation_logs l JOIN fields f ON l.fieldId = f.id WHERE f.owner_id = ?";
        List<IrrigationLog> list = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, farmerId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) list.add(mapResultSetToEntity(rs));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}