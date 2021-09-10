package com.es.phoneshop.web;

import com.es.phoneshop.exception.EntityNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ProductDao;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private Product product1;

    @InjectMocks
    private final ProductDetailPageServlet servlet = new ProductDetailPageServlet();

    @Before
    public void setup(){
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
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
        product1.setId(101L);

        when(request.getPathInfo()).thenReturn("/101");
        when(productDao.getProduct(101L)).thenReturn(Optional.of(product1));

        servlet.doGet(request, response);

        verify(productDao, times(1)).getProduct(101L);
        verify(request).setAttribute("product", product1);
    }

    @Test
    public void testRedirect() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/");

        servlet.doGet(request, response);

        verify(request).getRequestDispatcher(request.getContextPath() + "/products").forward(request, response);
    }


}