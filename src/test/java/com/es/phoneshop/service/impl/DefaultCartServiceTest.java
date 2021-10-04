package com.es.phoneshop.service.impl;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCartServiceTest {

    @Mock
    private HttpSession session;
    @Mock
    private HttpServletRequest request;
    @Mock
    private Cart cart;
    @Mock
    private Product product1;
    @Mock
    private Product product2;

    @InjectMocks
    private final CartService cartService = DefaultCartService.getInstance();

    private static final String ATTRIBUTE_NAME = DefaultCartService.class.getName() + ".cart";

    private void setupProduct(Product product, Long id, int stock, BigDecimal price) {
        when(product.getId()).thenReturn(id);
        when(product.getStock()).thenReturn(stock);
        when(product.getPrice()).thenReturn(price);
    }

    @Before
    public void setup() {
        setupProduct(product1, 101L, 101, BigDecimal.valueOf(101));
        setupProduct(product2, 102L, 102, BigDecimal.valueOf(102));
    }

    @Test
    public void testAddProductToEmptyCart() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(ATTRIBUTE_NAME)).thenReturn(null);
        Cart cart = cartService.getCart(request);
        cartService.add(cart, product1, 5);
        verify(session, times(1)).setAttribute(ATTRIBUTE_NAME, new Cart(Map.of(product1.getId(), new CartItem(product1, 5))));
        assertEquals(product1, cart.getItems().get(product1.getId()).getProduct());
    }

    @Test
    public void testAddProductToNonEmptyCart() {
        Cart cart = new Cart(Map.of(product1.getId(), new CartItem(product1, 5)));
        cartService.add(cart, product2, 4);

        Map<Long, CartItem> expected = Map.of(
                product2.getId(), new CartItem(product2, 4),
                product1.getId(), new CartItem(product1, 5)
        );

        assertEquals(2, cart.getItems().size());
        assertEquals(expected, cart.getItems());
    }

    @Test
    public void testAddToCartExistingProduct() {
        Cart cart = new Cart(Map.of(product1.getId(), new CartItem(product1, 5)));
        cartService.add(cart, product1, 4);
        assertEquals(9L, cart.getItems().get(product1.getId()).getQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddToCartTooMuchExistingProduct() {
        Cart cart = new Cart(Map.of(product1.getId(), new CartItem(product1, 5)));
        cartService.add(cart, product1, 97);
        assertEquals(102L, cart.getItems().get(product1.getId()).getQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddToCartTooMuchProducts() {
        cartService.add(cart, product1, 102);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToCartProductWithInvalidQuantity() {
        cartService.add(cart, product1, 0);
    }

    @Test
    public void testUpdateCartCorrectly() {
        Cart cart = new Cart(Map.of(product1.getId(), new CartItem(product1, 5)));
        cartService.update(cart, product1, 8);
        assertEquals(8, cart.getItems().get(product1.getId()).getQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testUpdateCartWithTooMuchProducts() {
        cartService.update(cart, product1, 102);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCartWithInvalidQuantity() {
        cartService.update(cart, product1, 0);
    }


}