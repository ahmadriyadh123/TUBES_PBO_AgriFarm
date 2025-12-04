package com.agrifarm.service;

import java.util.List;

public interface GenericService<T> {
    void create(T entity);

    void update(T entity);

    void delete(int id);

    T getById(int id);

    List<T> getAll();
}