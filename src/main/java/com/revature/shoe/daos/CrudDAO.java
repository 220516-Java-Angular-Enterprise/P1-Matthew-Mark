package com.revature.shoe.daos;

import java.util.List;

public interface CrudDAO<T> {

    void save(T obj);

    void update(T obj);

    void delete(String obj);

    T getById(String id);

    List<T> getAll();

}
