package com.agrifarm.service;

import com.agrifarm.dao.FieldDAO;
import com.agrifarm.dao.GenericDAO;
import com.agrifarm.model.Field;

public class FieldService extends AbstractService<Field> {

    private final FieldDAO fieldDAO;

    public FieldService(FieldDAO fieldDAO) {
        this.fieldDAO = fieldDAO;
    }

    @Override
    protected GenericDAO<Field> getDAO() {
        return fieldDAO;
    }

    // Method spesifik yang tidak ada di Generic
    public Field getFieldByFarmer(int farmerId) {
        return fieldDAO.getByFarmer(farmerId);
    }
    
    // Method assignField (Logic spesifik)
    public void assignField(Field field, int farmerId) {
        if (field.getSize() <= 0) {
            System.out.println(">> [ERROR] Ukuran lahan tidak valid.");
            return;
        }
        fieldDAO.save(field, farmerId);
    }
}