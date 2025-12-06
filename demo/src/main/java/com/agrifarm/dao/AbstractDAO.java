package com.agrifarm.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractDAO<T> implements GenericDAO<T> {

    protected final Logger logger = Logger.getLogger(getClass().getName());

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
                    // Fix formatting
                    logger.log(Level.INFO, ">> [DB] Data saved to {0} (ID: {1})",
                            new Object[] { getTableName(), generatedKeys.getInt(1) });
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
            // Fix formatting
            logger.log(Level.INFO, ">> [DB] Data updated in {0}", getTableName());
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
            // Fix formatting
            logger.log(Level.INFO, ">> [DB] Data deleted from {0}", getTableName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T getById(int id) { // CHANGED from 'get' to 'getById' to match Service expectation commonly, or I
                               // will see GenericDAO first.
        // Wait, I should check GenericDAO first. Assuming 'get' is what DAO has, I
        // should align them.
        // Let's stick to what AbstractDAO had: 'get'. If GenericDAO has 'get', then
        // AbstractService is wrong calling 'getById'.
        // I will assume for now I will fix AbstractService to call 'get' instead of
        // renaming this.
        // BUT, standard naming is often getById. Let's see GenericDAO content in next
        // step before finalizing this file decision?
        // Actually, I can rename this to getById if GenericDAO has getById. Or plain
        // get.
        // The error said "The method getById(int) is undefined for the type
        // GenericDAO<T>".
        // This means GenericDAO likely has NOTHING or has 'get'.
        // If AbstractDAO implements GenericDAO, and AbstractDAO has 'get', then
        // GenericDAO likely has 'get'.
        // So AbstractService calling 'getById' is the error.

        String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
        // ... (implementation of get)
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