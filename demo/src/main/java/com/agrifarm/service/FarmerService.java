package com.agrifarm.service;

import com.agrifarm.dao.FarmerDAO;
import com.agrifarm.dao.IrrigationLogDAO;
import com.agrifarm.model.Farmer;
import com.agrifarm.model.IrrigationLog;

import java.util.List;

public class FarmerService {

    private final FarmerDAO farmerDAO;
    private final IrrigationLogDAO irrigationLogDAO;

    public FarmerService(FarmerDAO farmerDAO, IrrigationLogDAO irrigationLogDAO) {
        this.farmerDAO = farmerDAO;
        this.irrigationLogDAO = irrigationLogDAO;
    }

    public void registerFarmer(Farmer farmer) {
        farmerDAO.save(farmer);
    }

    public List<Farmer> getAllFarmers() {
        return farmerDAO.getAll();
    }

    // Business Logic: Riwayat irigasi per petani
    public List<IrrigationLog> getIrrigationHistoryByFarmer(int farmerId) {
        return irrigationLogDAO.getByFarmer(farmerId);
    }
}
