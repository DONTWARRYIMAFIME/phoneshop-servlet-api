package com.es.phoneshop.model.product.viewed;

import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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
    private Product product5;
    @Mock
    private Product product6;

    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;

    private final RecentlyViewedHistoryService viewedService = DefaultRecentlyViewedHistory.getInstance();
    private final RecentlyViewedHistory viewed = new RecentlyViewedHistory();

    @Before
    public void setup() {
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testGetRecentlyViewedHistory() {
        RecentlyViewedHistory viewed = viewedService.getRecentlyViewedHistory(request);
        assertNotNull(viewed);
    }

    @Test
    public void testAddToRecentViewedHistory() {
        assertFalse(viewed.getProducts().contains(product1));
        assertFalse(viewed.getProducts().contains(product2));
        assertFalse(viewed.getProducts().contains(product3));

        viewed.addProduct(product1);
        viewed.addProduct(product2);
        viewed.addProduct(product3);

        assertTrue(viewed.getProducts().contains(product1));
        assertTrue(viewed.getProducts().contains(product2));
        assertTrue(viewed.getProducts().contains(product3));

        assertEquals(product3, viewed.getProducts().getFirst());
        assertEquals(product1, viewed.getProducts().getLast());
    }

    @Test
    public void testRecentViewedHistorySize() {
        viewed.addProduct(product1);
        viewed.addProduct(product2);
        viewed.addProduct(product3);
        viewed.addProduct(product4);
        viewed.addProduct(product5);
        viewed.addProduct(product6);

        assertEquals(3, viewed.getProducts().size());
    }

    @Test
    public void testAddingTheSameProduct() {
        viewed.addProduct(product1);
        viewed.addProduct(product1);
        viewed.addProduct(product1);

        assertEquals(1, viewed.getProducts().size());

        viewed.addProduct(product2);
        viewed.addProduct(product3);

        assertEquals(product3, viewed.getProducts().getFirst());
        assertEquals(product1, viewed.getProducts().getLast());

        viewed.addProduct(product1);
        viewed.addProduct(product1);
        viewed.addProduct(product1);

        assertEquals(3, viewed.getProducts().size());
        assertEquals(product1, viewed.getProducts().getFirst());
        assertEquals(product2, viewed.getProducts().getLast());
    }

}