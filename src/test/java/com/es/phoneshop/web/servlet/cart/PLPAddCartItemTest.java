package com.es.phoneshop.web.servlet.cart;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.EntityNotFoundException;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PLPAddCartItemTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private ProductDao productDao;
    @Mock
    private CartService cartService;

    @Mock
    private Cart cart;
    @Mock
    private Product product;

    @InjectMocks
    private final PLPAddCartItemServlet servlet = new PLPAddCartItemServlet();

    @Before
    public void setup() {
        when(request.getPathInfo()).thenReturn("/101");
        when(productDao.find(101L)).thenReturn(Optional.of(product));
        when(cartService.getCart(request)).thenReturn(cart);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
    }

    @Test
    public void testDoPostWithCorrectParams() throws IOException {
        int quantity = 1;
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));

        servlet.doPost(request, response);

        verify(cartService, times(1)).add(cart, product, quantity);
        verify(response, times(1)).sendRedirect(any());
    }

    @Test(expected = NumberFormatException.class)
    public void testDoPostWithInvalidProductId() throws IOException {
        when(request.getPathInfo()).thenReturn("/something");
        servlet.doPost(request, response);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetProductWithNonExistingProductId() throws IOException {
        when(request.getPathInfo()).thenReturn("/-5");
        servlet.doPost(request, response);
    }

    @Test
    public void testDoPostWithInvalidQuantity() throws IOException {
        when(request.getParameter("quantity")).thenReturn("invalid quantity");
        servlet.doPost(request, response);
        verify(response, times(1)).sendRedirect(any());
    }

    @Test
    public void testDoPostWithTooMuchQuantity() throws IOException {
        int quantity = 3000;
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));

        doThrow(new OutOfStockException(quantity, product.getStock())).when(cartService).add(cart, product, quantity);

        servlet.doPost(request, response);

        verify(cartService, times(1)).add(cart, product, quantity);
        verify(cart, never()).addItem(product.getId(), new CartItem(product, quantity));
        verify(response, times(1)).sendRedirect(any());
    }

    @Test
    public void testDoPostWithNegativeQuantity() throws IOException{
        int quantity = -5;
        when(request.getParameter("quantity")).thenReturn(String.valueOf(quantity));

        doThrow(new IllegalArgumentException("Quantity must be more then 0")).when(cartService).add(cart, product, quantity);

        servlet.doPost(request, response);

        verify(cartService, times(1)).add(cart, product, quantity);
        verify(cart, never()).addItem(product.getId(), new CartItem(product, quantity));
        verify(response, times(1)).sendRedirect(any());
    }

}
