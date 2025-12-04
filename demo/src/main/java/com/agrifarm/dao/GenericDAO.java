package com.agrifarm.dao;

import java.util.List;

public interface GenericDAO<T> {
    void save(T t);
    void update(T t);
    void delete(int id);
    T get(int id);
    List<T> getAll();
}
