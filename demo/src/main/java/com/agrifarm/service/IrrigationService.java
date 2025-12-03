package com.agrifarm.service;

import com.agrifarm.dao.GenericDAO;
import com.agrifarm.dao.IrrigationLogDAO;
import com.agrifarm.dao.PlantDAO;
import com.agrifarm.model.IrrigationLog;
import com.agrifarm.model.Plant;

import java.util.List;

// 1. Extends AbstractService agar punya fitur CRUD standar
public class IrrigationService extends AbstractService<IrrigationLog> {

    private final IrrigationLogDAO irrigationLogDAO;
    private final PlantDAO plantDAO;

    // 2. Constructor Injection (Menyuntikkan DAO yang dibutuhkan)
    public IrrigationService(IrrigationLogDAO irrigationLogDAO, PlantDAO plantDAO) {
        this.irrigationLogDAO = irrigationLogDAO;
        this.plantDAO = plantDAO;
    }

    // 3. Implementasi method abstrak dari AbstractService
    @Override
    protected GenericDAO<IrrigationLog> getDAO() {
        return irrigationLogDAO; // Memberi tahu parent service DAO mana yang dipakai
    }

    // =================================================================
    // LOGIKA BISNIS SPESIFIK (Yang tidak dimiliki AbstractService)
    // =================================================================

    /**
     * Fitur Batch Irrigation: Menyiram seluruh tanaman di satu lahan sekaligus.
     * Menggunakan PlantDAO untuk mencari tanaman, lalu mencatat Log.
     */
    public void irrigateField(int fieldId, double waterPerPlant) {
        List<Plant> plants = plantDAO.getByField(fieldId);

        if (plants.isEmpty()) {
            System.out.println(">> [INFO] Tidak ada tanaman di Field ID " + fieldId);
            return;
        }

        System.out.println(">> [PROCESS] Memulai irigasi massal untuk Field ID " + fieldId);

        for (Plant plant : plants) {
            // Logika: Tambah progress pertumbuhan tanaman (Simulasi)
            // plant.grow(10); // Jika entity Plant punya method update di DAO, bisa dipanggil di sini.

            // Membuat Log Irigasi
            // Catatan: Model IrrigationLog disesuaikan dengan field yang ada (fieldId, volume)
            IrrigationLog log = new IrrigationLog(fieldId, waterPerPlant);
            
            // Simpan menggunakan method 'create' milik parent (AbstractService) 
            // agar validasi & logging standar berjalan.
            super.create(log); 
        }
        System.out.println(">> [SUCCESS] " + plants.size() + " tanaman telah disiram.");
    }

    /**
     * Mengambil riwayat irigasi berdasarkan Petani.
     * Method ini memanggil fungsi spesifik di IrrigationLogDAO.
     */
    public List<IrrigationLog> getLogsByFarmer(int farmerId) {
        return irrigationLogDAO.getByFarmer(farmerId);
    }
}