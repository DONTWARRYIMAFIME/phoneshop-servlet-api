package com.es.phoneshop.web.cart;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
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

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MiniCartServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private CartService cartService;

    @Mock
    private Cart cart;

    @InjectMocks
    private final MiniCartServlet servlet = new MiniCartServlet();

    private final String JSP_PATH = "/WEB-INF/pages/minicart.jsp";

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(cartService.getCart(request)).thenReturn(cart);
        when(request.getRequestDispatcher(JSP_PATH)).thenReturn(requestDispatcher);

        servlet.doGet(request,response);

        verify(request, times(1)).setAttribute("cart", cart);
        verify(request, times(1)).getRequestDispatcher(JSP_PATH);
        verify(requestDispatcher, times(1)).include(request, response);
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        when(cartService.getCart(request)).thenReturn(cart);
        when(request.getRequestDispatcher(JSP_PATH)).thenReturn(requestDispatcher);

        servlet.doPost(request,response);

        verify(servlet, times(1)).doGet(request, response);
    }

}
