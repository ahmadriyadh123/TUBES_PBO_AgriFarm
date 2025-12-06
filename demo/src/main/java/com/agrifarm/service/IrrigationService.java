package com.agrifarm.service;

import com.agrifarm.dao.GenericDAO;
import com.agrifarm.dao.IrrigationLogDAO;
import com.agrifarm.dao.PlantDAO;
import com.agrifarm.model.IrrigationLog;
import com.agrifarm.model.Plant;

import java.util.*;
import java.util.logging.Level;

public class IrrigationService extends AbstractService<IrrigationLog> {

    private final IrrigationLogDAO irrigationLogDAO;
    private final PlantDAO plantDAO;

    public IrrigationService(IrrigationLogDAO irrigationLogDAO, PlantDAO plantDAO) {
        this.irrigationLogDAO = irrigationLogDAO;
        this.plantDAO = plantDAO;
    }

    @Override
    protected GenericDAO<IrrigationLog> getDAO() {
        return irrigationLogDAO;
    }

    public void irrigateField(int fieldId, double waterPerPlant) {
        List<Plant> plants = plantDAO.getByField(fieldId);

        if (plants.isEmpty()) {
            logger.log(Level.INFO, ">> [INFO] Tidak ada tanaman di Field ID {0}", fieldId);
            return;
        }

        logger.log(Level.INFO, ">> [PROCESS] Memulai irigasi massal untuk Field ID {0}", fieldId);

        for (int i = 0; i < plants.size(); i++) {
            // Unused 'plant' variable removed or used for logging if needed.
            // Loop is just to count or trigger events.

            IrrigationLog log = new IrrigationLog(fieldId, waterPerPlant);
            super.create(log);
        }
        logger.log(Level.INFO, ">> [SUCCESS] {0} tanaman telah disiram.", plants.size());
    }

    public List<IrrigationLog> getLogsByFarmer(int farmerId) {
        return irrigationLogDAO.getByFarmer(farmerId);
    }

    public Map<Integer, Double> getWaterUsageStats(int farmerId) {
        List<IrrigationLog> logs = getLogsByFarmer(farmerId);

        Map<Integer, Double> statsMap = new HashMap<>();

        for (IrrigationLog log : logs) {
            int fId = log.getFieldId();
            double vol = log.getWaterVolume();

            statsMap.put(fId, statsMap.getOrDefault(fId, 0.0) + vol);
        }

        return statsMap;
    }

    public List<IrrigationLog> getSortedLogsByDate(int farmerId) {
        List<IrrigationLog> logs = getLogsByFarmer(farmerId);

        Collections.sort(logs, (o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));

        return logs;
    }
}