package com.agrifarm.service;

import com.agrifarm.dao.GenericDAO;
import java.util.List;

public abstract class AbstractService<T> implements GenericService<T> {

    // Service butuh DAO untuk akses data
    protected abstract GenericDAO<T> getDAO();

    @Override
    public void create(T entity) {
        // CONTOH LOGIC GENERIC: Validasi Data Null
        if (entity == null) {
            System.out.println(">> [ERROR] Data tidak boleh kosong!");
            return;
        }
        
        // Teruskan ke DAO
        getDAO().save(entity);
        
        // CONTOH LOGIC GENERIC: Logging Otomatis
        System.out.println(">> [LOG] Data baru berhasil ditambahkan via Service.");
    }

    @Override
    public void update(T entity) {
        getDAO().update(entity);
        System.out.println(">> [LOG] Data berhasil diperbarui via Service.");
    }

    @Override
    public void delete(int id) {
        // CONTOH LOGIC GENERIC: Cek keberadaan data sebelum hapus
        if (getDAO().get(id) != null) {
            getDAO().delete(id);
            System.out.println(">> [LOG] Data ID " + id + " telah dihapus.");
        } else {
            System.out.println(">> [ERROR] Data ID " + id + " tidak ditemukan, gagal hapus.");
        }
    }

    @Override
    public T getById(int id) {
        return getDAO().get(id);
    }

    @Override
    public List<T> getAll() {
        return getDAO().getAll();
    }
}