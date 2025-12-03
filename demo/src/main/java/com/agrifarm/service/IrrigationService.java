package com.agrifarm.service;

import com.agrifarm.dao.IrrigationLogDAO;
import com.agrifarm.dao.PlantDAO;
import com.agrifarm.model.IrrigationLog;
import com.agrifarm.model.Plant;

import java.time.LocalDateTime;
import java.util.List;

public class IrrigationService {

    private final PlantDAO plantDAO;
    private final IrrigationLogDAO irrigationLogDAO;

    public IrrigationService(PlantDAO plantDAO, IrrigationLogDAO irrigationLogDAO) {
        this.plantDAO = plantDAO;
        this.irrigationLogDAO = irrigationLogDAO;
    }

    // Business Logic: Menyiram semua tanaman pada satu lahan
    public void irrigateField(int fieldId, int farmerId) {
        List<Plant> plants = plantDAO.getByField(fieldId);

        for (Plant plant : plants) {
            IrrigationLog log = new IrrigationLog(
                    farmerId,
                    plant.getId(),
                    fieldId,
                    LocalDateTime.now()
            );
            irrigationLogDAO.save(log);
        }
    }

    // Menampilkan riwayat irigasi
    public List<IrrigationLog> getAllLogs() {
        return irrigationLogDAO.getAll();
    }
}
