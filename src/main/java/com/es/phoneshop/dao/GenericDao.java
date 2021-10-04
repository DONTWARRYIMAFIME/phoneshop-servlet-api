package com.es.phoneshop.dao;

import com.es.phoneshop.model.Entity;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T extends Entity> {

    void save(T entity);

    Optional<T> find(Long id);

    List<T> findAll();

    void delete(Long id);

}