package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.exception.EntityNotFoundException;
import com.es.phoneshop.model.order.Order;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private OrderDao orderDao;
    @Mock
    private Order order;

    @InjectMocks
    private final OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);
        when(request.getPathInfo()).thenReturn("/secure-id");
    }

    @Test
    public void testDoGetWithValidSecureId() throws ServletException, IOException {
        when(orderDao.getOrderBySecureId(any())).thenReturn(Optional.of(order));

        servlet.doGet(request, response);

        verify(request, times(1)).setAttribute("order", order);
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testDoGetWithInvalidSecureId() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request, never()).setAttribute("order", order);
        verify(requestDispatcher, never()).forward(request, response);
    }

}
