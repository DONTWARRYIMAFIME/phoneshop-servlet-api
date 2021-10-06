package com.es.phoneshop.web.servlet.cart;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.exception.EntityNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ProductDao productDao;
    @Mock
    private CartService cartService;

    @Mock
    private Cart cart;
    @Mock
    private Product product1;
    @Mock
    private Product product2;
    @Mock
    private Product product3;

    @InjectMocks
    private final CartPageServlet servlet = new CartPageServlet();

    private final String JSP_PATH = "/WEB-INF/pages/cart.jsp";

    @Before
    public void setup() {
        when(request.getRequestDispatcher(JSP_PATH)).thenReturn(requestDispatcher);
        when(cartService.getCart(request)).thenReturn(cart);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);

        when(productDao.find(101L)).thenReturn(Optional.of(product1));
        when(productDao.find(102L)).thenReturn(Optional.of(product2));
        when(productDao.find(103L)).thenReturn(Optional.of(product3));

        when(product1.getId()).thenReturn(101L);
        when(product1.getId()).thenReturn(102L);
        when(product1.getId()).thenReturn(103L);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(request, times(1)).getRequestDispatcher(JSP_PATH);
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test
    public void testDoPostWithCorrectParameters() throws IOException, ServletException {
        when(request.getParameterValues("productId")).thenReturn(new String[] { "101", "102", "103" });
        when(request.getParameterValues("quantity")).thenReturn(new String[] { "1", "2", "3" });

        servlet.doPost(request, response);

        verify(response, times(1)).sendRedirect(any());

        verify(request, never()).setAttribute(eq("errors"), any());
        verify(requestDispatcher, never()).forward(request, response);
    }

    @Test(expected = NumberFormatException.class)
    public void testDoPostWithInvalidProductId() throws IOException, ServletException {
        when(request.getParameterValues("productId")).thenReturn(new String[] { "smth", "102", "103" });
        servlet.doPost(request, response);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDoPostWithNonExistingProductId() throws IOException, ServletException {
        when(request.getParameterValues("productId")).thenReturn(new String[] { "100", "102", "103" });
        servlet.doPost(request, response);
    }

    @Test
    public void testDoPostWithInvalidQuantity() throws IOException, ServletException {
        when(request.getParameterValues("productId")).thenReturn(new String[] { "101", "102", "103" });
        when(request.getParameterValues("quantity")).thenReturn(new String[] { "eee", "8", "smth" });

        servlet.doPost(request, response);

        verify(request, times(1)).setAttribute(eq("errors"), any());
        verify(requestDispatcher, times(1)).forward(request, response);

        verify(response, never()).sendRedirect(any());
    }

    @Test
    public void testDoPostWithTooMuchQuantity() throws IOException, ServletException {
        when(request.getParameterValues("productId")).thenReturn(new String[] { "101", "102", "103" });
        when(request.getParameterValues("quantity")).thenReturn(new String[] { "3000", "2", "3" });

        doThrow(IllegalArgumentException.class).when(cartService).update(cart, product1, 3000);

        servlet.doPost(request, response);

        verify(request, times(1)).setAttribute(eq("errors"), any());
        verify(requestDispatcher, times(1)).forward(request, response);

        verify(response, never()).sendRedirect(any());
    }

    @Test
    public void testDoPostWithNegativeQuantity() throws IOException, ServletException {
        when(request.getParameterValues("productId")).thenReturn(new String[] { "101", "102", "103" });
        when(request.getParameterValues("quantity")).thenReturn(new String[] { "-5", "2", "3" });

        doThrow(IllegalArgumentException.class).when(cartService).update(cart, product1, -5);

        servlet.doPost(request, response);

        verify(request, times(1)).setAttribute(eq("errors"), any());
        verify(requestDispatcher, times(1)).forward(request, response);

        verify(response, never()).sendRedirect(any());
    }

}
