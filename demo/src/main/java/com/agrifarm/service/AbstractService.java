package com.agrifarm.service;

import com.agrifarm.dao.GenericDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractService<T> implements GenericService<T> {

    protected final Logger logger = Logger.getLogger(getClass().getName());

    protected abstract GenericDAO<T> getDAO(); // Template Method

    @Override
    public void create(T entity) {
        if (entity == null) {
            logger.severe(">> [ERROR] Data tidak boleh kosong!");
            return;
        }
        getDAO().save(entity);
        logger.info(">> [LOG] Data baru berhasil ditambahkan via Service.");
    }

    @Override
    public T getById(int id) {
        T data = getDAO().get(id); // Changed from getById to get to match GenericDAO
        if (data != null) {
            // Fix: Use formatting
            logger.log(Level.INFO, ">> [LOG] Data ditemukan: {0}", data);
        } else {
            logger.log(Level.WARNING, ">> [LOG] Data dengan ID {0} tidak ditemukan.", id);
        }
        return data;
    }

    @Override
    public List<T> getAll() {
        return getDAO().getAll();
    }

    @Override
    public void update(T entity) {
        if (entity == null)
            return;
        getDAO().update(entity);
        logger.info(">> [LOG] Data berhasil diperbarui.");
    }

    @Override
    public void delete(int id) {
        getDAO().delete(id);
        logger.log(Level.INFO, ">> [LOG] Data dengan ID {0} telah dihapus.", id);
    }
}