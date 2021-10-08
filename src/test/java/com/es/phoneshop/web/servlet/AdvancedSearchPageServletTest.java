package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdvancedSearchPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private final AdvancedSearchPageServlet servlet = new AdvancedSearchPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testFirstDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request, never()).setAttribute("products", productDao.findProducts(any()));
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test
    public void testSecondDoGet() throws ServletException, IOException {
        when(request.getParameterMap()).thenReturn(Map.of("query", new String[]{"smth"}));

        servlet.doGet(request, response);

        verify(request, times(1)).setAttribute(eq("searchModes"), any());
        verify(request, times(1)).setAttribute("products", productDao.findProducts(any()));
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test
    public void testDoGetWithCorrectParams() throws ServletException, IOException {
        when(request.getParameterMap()).thenReturn(Map.of("minStock", new String[]{"10"}, "maxStock", new String[]{"50"}));
        when(request.getParameter("minStock")).thenReturn("10");
        when(request.getParameter("maxStock")).thenReturn("50");

        servlet.doGet(request, response);

        verify(request, times(1)).setAttribute("errors", Map.of());
        verify(request, times(1)).setAttribute(eq("searchModes"), any());
        verify(request, times(1)).setAttribute("products", productDao.findProducts(any()));
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test
    public void testDoGetWithIncorrectParams() throws ServletException, IOException {
        when(request.getParameterMap()).thenReturn(Map.of("minStock", new String[]{"10"}, "maxStock", new String[]{"eee"}));
        when(request.getParameter("maxStock")).thenReturn("eee");
        when(request.getParameter("minStock")).thenReturn("10");

        servlet.doGet(request, response);

        verify(request, times(1)).setAttribute("errors", Map.of("maxStock", "Invalid value"));
        verify(request, times(1)).setAttribute(eq("searchModes"), any());
        verify(request, times(1)).setAttribute("products", productDao.findProducts(any()));
        verify(requestDispatcher, times(1)).forward(request, response);
    }

}