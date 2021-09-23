package com.es.phoneshop.web;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.service.RecentlyViewedHistoryService;
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
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ProductDao productDao;
    @Mock
    private Product product;
    @Mock
    private RecentlyViewedHistoryService viewedService;
    @Mock
    private LinkedList<Product> viewed;

    @InjectMocks
    private final ProductListPageServlet servlet = new ProductListPageServlet();

    @Before
    public void setup(){
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(productDao.findProducts(null, null, null)).thenReturn(List.of(product));
        when(viewedService.getRecentlyViewedHistory(request)).thenReturn(viewed);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute("products", List.of(product));
        verify(request).setAttribute("viewed", viewed);
        verify(requestDispatcher).forward(request, response);
    }

}