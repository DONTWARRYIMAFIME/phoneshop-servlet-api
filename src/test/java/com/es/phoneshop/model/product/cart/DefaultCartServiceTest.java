package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.exception.IllegalProductQuantityException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {

    @Mock
    private Product product;
    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;

    private final CartService cartService = DefaultCartService.getInstance();
    private final Cart cart = new Cart();

    @Before
    public void setup() {
        when(product.getId()).thenReturn(101L);
        when(product.getStock()).thenReturn(101);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testGetCart() {
        Cart cart = cartService.getCart(request);
        assertNotNull(cart);
    }

    @Test
    public void testAddToCartCorrectProduct() {
        cartService.add(cart, product, 1);
        assertEquals(1L, cart.getItems().get(product.getId()).getQuantity());
        assertNotNull(cart.getItems().get(product.getId()));
    }

    @Test
    public void testAddToCartExistingProduct() {
        cartService.add(cart, product, 1);
        assertEquals(1L, cart.getItems().get(product.getId()).getQuantity());
        cartService.add(cart, product, 5);
        assertEquals(6L, cart.getItems().get(product.getId()).getQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddToCartTooMuchExistingProduct() {
        cartService.add(cart, product, 1);
        assertEquals(1L, cart.getItems().get(product.getId()).getQuantity());
        cartService.add(cart, product, 100);
        assertEquals(101L, cart.getItems().get(product.getId()).getQuantity());
        cartService.add(cart, product, 1);
    }

    @Test(expected = OutOfStockException.class)
    public void testAddToCartTooMuchProducts() {
        cartService.add(cart, product, 102);
    }

    @Test(expected = IllegalProductQuantityException.class)
    public void testAddToCartProductWithInvalidQuantity() {
        cartService.add(cart, product, 0);
    }

}