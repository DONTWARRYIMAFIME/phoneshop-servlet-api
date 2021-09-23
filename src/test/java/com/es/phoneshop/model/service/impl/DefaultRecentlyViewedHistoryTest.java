package com.es.phoneshop.model.service.impl;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.RecentlyViewedHistoryService;
import com.es.phoneshop.service.impl.DefaultRecentlyViewedHistoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRecentlyViewedHistoryTest {

    @Mock
    private Product product1;
    @Mock
    private Product product2;
    @Mock
    private Product product3;
    @Mock
    private Product product4;

    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;

    private final RecentlyViewedHistoryService viewedService = DefaultRecentlyViewedHistoryService.getInstance();
    private static final String ATTRIBUTE_NAME = DefaultRecentlyViewedHistoryService.class.getName() + ".viewed";

    @Test
    public void testAddProductToEmptyHistory() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(ATTRIBUTE_NAME)).thenReturn(null);
        LinkedList<Product> viewed = viewedService.getRecentlyViewedHistory(request);
        assertNotNull(viewed);
        viewedService.addProduct(viewed, product1);
        verify(session, times(1)).setAttribute(ATTRIBUTE_NAME, List.of(product1));
    }

    @Test
    public void testAddProductToNonEmptyHistory() {
        LinkedList<Product> viewed = new LinkedList<>(List.of(product1, product2));
        viewedService.addProduct(viewed, product3);
        assertEquals(List.of(product3, product1, product2), viewed);
    }

    @Test
    public void testMaxHistorySize() {
        LinkedList<Product> viewed = new LinkedList<>(List.of(product1, product2, product3));
        viewedService.addProduct(viewed, product4);
        assertEquals(3, viewed.size());
        assertEquals(List.of(product4, product1, product2), viewed);
    }

    @Test
    public void testAddDuplicatedProductsToHistory() {
        LinkedList<Product> viewed = new LinkedList<>(List.of(product1, product2));
        viewedService.addProduct(viewed, product2);
        viewedService.addProduct(viewed, product1);
        assertEquals(2, viewed.size());
        assertEquals(List.of(product1, product2), viewed);
    }

}