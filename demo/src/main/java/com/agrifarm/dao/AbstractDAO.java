package com.agrifarm.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO<T> implements GenericDAO<T> {

    protected abstract String getTableName();
    protected abstract String getInsertSql();
    protected abstract String getUpdateSql();
    protected abstract void setInsertParameters(PreparedStatement ps, T entity) throws SQLException;
    protected abstract void setUpdateParameters(PreparedStatement ps, T entity) throws SQLException;
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

    @Override
    public void save(T entity) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(getInsertSql(), Statement.RETURN_GENERATED_KEYS)) {
            
            setInsertParameters(ps, entity);
            ps.executeUpdate();
            
            // Opsional: Ambil Generated ID jika perlu
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    System.out.println(">> [DB] Data saved to " + getTableName() + " (ID: " + generatedKeys.getInt(1) + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(T entity) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(getUpdateSql())) {
            
            setUpdateParameters(ps, entity);
            ps.executeUpdate();
            System.out.println(">> [DB] Data updated in " + getTableName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println(">> [DB] Data deleted from " + getTableName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T get(int id) {
        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName();
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}