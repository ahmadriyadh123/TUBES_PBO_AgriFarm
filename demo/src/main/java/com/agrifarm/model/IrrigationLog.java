package com.agrifarm.model;

import java.time.LocalDateTime;

public class IrrigationLog {
    private int id;
    private int fieldId;
    private double waterVolume;
    private LocalDateTime timestamp;

    // Constructor untuk pengambilan data dari DB (dengan ID dan timestamp)
    public IrrigationLog(int id, int fieldId, double waterVolume, LocalDateTime timestamp) {
        this.id = id;
        this.fieldId = fieldId;
        this.waterVolume = waterVolume;
        this.timestamp = timestamp;
    }

    // Constructor untuk pembuatan data baru (tanpa ID, timestamp otomatis)
    // Digunakan di Main.java dan IrrigationService
    public IrrigationLog(int fieldId, double waterVolume) {
        this.fieldId = fieldId;
        this.waterVolume = waterVolume;
        this.timestamp = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public double getWaterVolume() {
        return waterVolume;
    }

    public void setWaterVolume(double waterVolume) {
        this.waterVolume = waterVolume;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
