package com.agrifarm.service;

import com.agrifarm.dao.FarmerDAO;
import com.agrifarm.dao.GenericDAO;
import com.agrifarm.model.Farmer;

// EXTENDS AbstractService -> Otomatis punya fitur create, update, delete, getAll
public class FarmerService extends AbstractService<Farmer> {

    private final FarmerDAO farmerDAO;

    // Constructor Injection
    public FarmerService(FarmerDAO farmerDAO) {
        this.farmerDAO = farmerDAO;
    }

    // Wajib implementasi ini agar AbstractService tahu DAO mana yang dipakai
    @Override
    protected GenericDAO<Farmer> getDAO() {
        return farmerDAO;
    }

    // --- LOGIKA BISNIS SPESIFIK (Yang tidak bisa digeneralisir) ---

    public void registerFarmer(Farmer farmer) {
        // Contoh Logic Spesifik: Validasi password minimal
        if (farmer.getPassword().length() < 3) {
            logger.warning(">> [VALIDASI] Password terlalu pendek!");
            return;
        }
        // Panggil method create milik Parent (AbstractService)
        super.create(farmer);
    }

    // Fitur Login sangat spesifik untuk User/Farmer, jadi ditulis manual di sini
    public Farmer login(String username, String password) {
        return farmerDAO.login(username, password);
    }
}
