package com.agrifarm.model;

import java.time.LocalDateTime;

public class IrrigationLog {

    private int id;
    private int fieldId;
    private double waterVolume;
    private LocalDateTime timestamp;

    // Constructor tanpa ID (untuk INSERT)
    public IrrigationLog(int fieldId, double waterVolume) {
        this.fieldId = fieldId;
        this.waterVolume = waterVolume;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor lengkap (untuk READ dari database)
    public IrrigationLog(int id, int fieldId, double waterVolume, LocalDateTime timestamp) {
        this.id = id;
        this.fieldId = fieldId;
        this.waterVolume = waterVolume;
        this.timestamp = timestamp;
    }

    // Getter & Setter
    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return "IrrigationLog{" +
                "id=" + id +
                ", fieldId=" + fieldId +
                ", waterVolume=" + waterVolume +
                ", timestamp=" + timestamp +
                '}';
    }
}
