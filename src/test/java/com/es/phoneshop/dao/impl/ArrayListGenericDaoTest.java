package com.es.phoneshop.dao.impl;

import com.es.phoneshop.model.Entity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListGenericDaoTest<T extends Entity> {
    @Mock
    private T entity1;
    @Mock
    private T entity2;
    @Mock
    private T entity3;
    @Mock
    private T entity4;
    @Mock
    private T entity5;

    @Spy
    private ArrayList<T> entities;

    @InjectMocks
    private ArrayListGenericDao<T> genericDao = Mockito.spy(ArrayListGenericDao.class);

    @Before
    public void setup() {
        when(entity1.getId()).thenReturn(101L);
        when(entity2.getId()).thenReturn(102L);
        when(entity3.getId()).thenReturn(103L);
        when(entity4.getId()).thenReturn(104L);
        entities.addAll(List.of(entity1, entity2, entity3));
    }


    @Test
    public void testFindAll() {
        assertEquals(List.of(entity1, entity2, entity3), genericDao.findAll());
    }

    @Test
    public void testFind() {
        assertEquals(Optional.of(entity1), genericDao.find(entity1.getId()));
        assertEquals(Optional.of(entity2), genericDao.find(entity2.getId()));
        assertEquals(Optional.of(entity3), genericDao.find(entity3.getId()));
    }

    @Test
    public void testSaveEntityWithId() {
        genericDao.save(entity4);
        assertEquals(List.of(entity1, entity2, entity3, entity4), entities);
    }

    @Test
    public void testSaveEntityWithOutId() {
        when(entity5.getId()).thenReturn(null);
        genericDao.save(entity5);
        assertEquals(List.of(entity1, entity2, entity3, entity5), entities);
    }

    @Test
    public void testUpdateEntity() {
        when(entity5.getId()).thenReturn(103L);
        genericDao.save(entity5);
        assertEquals(List.of(entity1, entity2, entity5), entities);
    }

    @Test
    public void delete() {
        genericDao.delete(101L);
        assertEquals(List.of(entity2, entity3), entities);
    }

}
