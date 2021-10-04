package com.es.phoneshop.dao.impl;

import com.es.phoneshop.model.Entity;
import com.es.phoneshop.dao.GenericDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class ArrayListGenericDao<T extends Entity> implements GenericDao<T> {

    private List<T> entities;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    protected final Lock writeLock = lock.writeLock();
    protected final Lock readLock = lock.readLock();

    protected ArrayListGenericDao() {
        entities = new ArrayList<>();
    }

    @Override
    public void save(T entity) {
        find(entity.getId()).ifPresentOrElse(
                oldEntity -> update(oldEntity, entity),
                () -> add(entity));
    }

    private void add(T entity) {
        writeLock.lock();
        try {
            if (entity.getId() == null) {
                entity.setId(getMaxId() + 1);
            }
            entities.add(entity);
        } finally {
            writeLock.unlock();
        }
    }

    private void update(T oldEntity, T newEntity) {
        writeLock.lock();
        try {
            int index = entities.indexOf(oldEntity);
            entities.set(index, newEntity);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Optional<T> find(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return entities
                .stream()
                .filter(e -> e.getId().equals(id))
                .findAny();
    }

    @Override
    public List<T> findAll() {
        readLock.lock();
        try {
            return entities;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void delete(Long id) {
        writeLock.lock();
        try {
            entities.removeIf(entity -> entity.getId().equals(id));
        } finally {
            writeLock.unlock();
        }
    }

    private Long getMaxId() {
        return entities
                .stream()
                .mapToLong(Entity::getId)
                .max()
                .orElse(0L);
    }
}
