package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.order.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListOrderDaoTest {
    @Mock
    private Order order1;
    @Mock
    private Order order2;
    @Mock
    private Order order3;
    @Spy
    private ArrayList<Order> orders;

    @InjectMocks
    private final OrderDao orderDao = ArrayListOrderDao.getInstance();

    private void setupOrder(Order order, String secureId) {
        when(order.getSecureId()).thenReturn(secureId);
    }

    @Before
    public void setup() {
        setupOrder(order1, "secure-id-101");
        setupOrder(order2, "secure-id-102");
        setupOrder(order3, "secure-id-103");

        orders.addAll(List.of(order1, order2, order3));
    }

    @Test
    public void testGetOrderWithCorrectSecureId() {
        assertEquals(Optional.of(order1), orderDao.getOrderBySecureId(order1.getSecureId()));
        assertEquals(Optional.of(order2), orderDao.getOrderBySecureId(order2.getSecureId()));
        assertEquals(Optional.of(order3), orderDao.getOrderBySecureId(order3.getSecureId()));
    }

    @Test
    public void testGetOrderWithIncorrectSecureId() {
        Optional<Order> order = orderDao.getOrderBySecureId("secure-id");
        assertFalse(order.isPresent());
    }

}
