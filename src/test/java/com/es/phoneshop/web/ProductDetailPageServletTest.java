package com.es.phoneshop.web;

import com.es.phoneshop.exception.EntityNotFoundException;
import com.es.phoneshop.exception.IllegalProductQuantityException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.viewed.RecentlyViewedHistory;
import com.es.phoneshop.model.product.viewed.RecentlyViewedHistoryService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailPageServletTest {

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
    private RecentlyViewedHistoryService viewedService;

    @Mock
    private Cart cart;
    @Mock
    private RecentlyViewedHistory viewed;
    @Mock
    private Product product;

    @InjectMocks
    private final ProductDetailPageServlet servlet = new ProductDetailPageServlet();

    @Before
    public void setup(){
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getPathInfo()).thenReturn("/101");
        when(productDao.getProduct(101L)).thenReturn(Optional.of(product));
        when(cartService.getCart(request)).thenReturn(cart);
        when(viewedService.getRecentlyViewedHistory(request)).thenReturn(viewed);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testGetProductWithIncorrectPathParam() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/-5");
        servlet.doGet(request, response);
    }

    @Test(expected = NumberFormatException.class)
    public void testGetProductWithBadPathParam() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/something");
        servlet.doGet(request, response);
    }

    @Test
    public void testGetProductWithCorrectPathParam() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(productDao, times(1)).getProduct(101L);
        verify(cartService, times(1)).getCart(request);
        verify(viewedService, times(1)).getRecentlyViewedHistory(request);

        verify(request).setAttribute("product", product);
        verify(request).setAttribute("cart", cart);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("1");
        servlet.doPost(request, response);
        verify(request, never()).setAttribute(eq("error"), any());
        verify(response, times(1)).sendRedirect(any());
    }

    @Test
    public void testParseExceptionHandle() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("something but number");
        servlet.doPost(request, response);
        verify(request, times(1)).setAttribute("error", "Not a number");
        verify(response, never()).sendRedirect(any());
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test
    public void testIllegalProductQuantityExceptionHandle() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("0");

        doThrow(IllegalProductQuantityException.class).when(cartService).add(cart, product, 0);

        servlet.doPost(request, response);

        verify(request, times(1)).setAttribute(eq("error"), any());
        verify(response, never()).sendRedirect(any());
        verify(requestDispatcher, times(1)).forward(request, response);
    }

}