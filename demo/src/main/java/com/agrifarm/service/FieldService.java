package com.agrifarm.service;

import com.agrifarm.dao.FieldDAO;
import com.agrifarm.model.Field;

import java.util.List;

public class FieldService {

    private final FieldDAO fieldDAO;

    public FieldService(FieldDAO fieldDAO) {
        this.fieldDAO = fieldDAO;
    }

    public void assignField(Field field) {
        fieldDAO.save(field);
    }

    public List<Field> getFieldsByFarmer(int farmerId) {
        return fieldDAO.getByFarmer(farmerId);
    }

}
