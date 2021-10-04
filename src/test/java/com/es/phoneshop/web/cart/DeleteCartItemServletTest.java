package com.es.phoneshop.web.cart;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeleteCartItemServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private CartService cartService;

    @Mock
    private Cart cart;

    @InjectMocks
    private final DeleteCartItemServlet servlet = new DeleteCartItemServlet();

    @Test
    public void testDoPost() throws IOException {
        when(request.getPathInfo()).thenReturn("/101");
        when(cartService.getCart(request)).thenReturn(cart);

        servlet.doPost(request,response);

        verify(cartService, times(1)).delete(cart, 101L);
        verify(response, times(1)).sendRedirect(any());
    }

}