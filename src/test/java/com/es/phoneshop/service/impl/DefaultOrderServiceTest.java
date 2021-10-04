package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private Cart cart;

    @InjectMocks
    private final OrderService orderService = DefaultOrderService.getInstance();

    @Test(expected = NullPointerException.class)
    public void testGetOrderWithEmptyCart() {
        orderService.createOrder(cart);
    }

    @Test
    public void testGetOrderWithNonEmptyCart() {
        when(cart.getTotalPrice()).thenReturn(BigDecimal.TEN);
        Order order = orderService.createOrder(cart);
        assertEquals(cart.getTotalPrice(), order.getSubtotal());
    }

    @Test
    public void testGetPaymentMethods() {
        assertEquals(List.of(PaymentMethod.values()), orderService.getPaymentMethods());
    }

    @Test
    public void testPlaceOrder() {
        Order order = new Order();
        orderService.placeOrder(order);
        assertNotNull(order.getSecureId());
        verify(orderDao, times(1)).save(order);
    }

}
