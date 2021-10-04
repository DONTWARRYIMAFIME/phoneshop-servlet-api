package com.es.phoneshop.web.servlet;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
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
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private CartService cartService;
    @Mock
    private OrderService orderService;
    @Mock
    private Cart cart;
    @Mock
    private Order order;
    @Mock
    private List<PaymentMethod> paymentMethods;

    @InjectMocks
    private CheckoutPageServlet servlet = new CheckoutPageServlet();

    @Before
    public void setup() {
        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);
        when(cartService.getCart(request)).thenReturn(cart);
        when(orderService.createOrder(cart)).thenReturn(order);

        when(orderService.getPaymentMethods()).thenReturn(paymentMethods);
    }

    private void setupRequestParams(String firstName, String lastName, String phone, String date, String address, String paymentMethod) {
        when(request.getParameter("firstName")).thenReturn(firstName);
        when(request.getParameter("lastName")).thenReturn(lastName);
        when(request.getParameter("phone")).thenReturn(phone);
        when(request.getParameter("deliveryDate")).thenReturn(date);
        when(request.getParameter("deliveryAddress")).thenReturn(address);
        when(request.getParameter("paymentMethod")).thenReturn(paymentMethod);
    }

    @Test
    public void testDoPostWithPassedValidation() throws ServletException, IOException {
        setupRequestParams("1", "1", "1", "2021-02-22", "address", String.valueOf(PaymentMethod.CASH));
        servlet.doPost(request, response);
        verify(orderService, times(1)).placeOrder(order);
        verify(cartService, times(1)).clear(cart);
        verify(response, times(1)).sendRedirect(any());
        verify(requestDispatcher, never()).forward(request, response);
    }

    @Test
    public void testDoPostWithInvalidDate() throws ServletException, IOException {
        setupRequestParams("1", "1", "1", "date", "address", String.valueOf(PaymentMethod.CASH));
        servlet.doPost(request, response);
        verify(response, never()).sendRedirect(any());
        verify(request).setAttribute("errors", Map.of("deliveryDate", "Invalid value"));
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test
    public void testDoPostWithInvalidPaymentMethod() throws ServletException, IOException {
        setupRequestParams("1", "1", "1", "2021-02-22", "address", "PaymentMethod");
        servlet.doPost(request, response);
        verify(response, never()).sendRedirect(any());
        verify(request).setAttribute("errors", Map.of("paymentMethod", "Invalid value"));
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test
    public void testDoPostWithEmptyFields() throws ServletException, IOException {
        setupRequestParams(null, null, null, null, null, null);
        servlet.doPost(request, response);
        verify(response, never()).sendRedirect(any());
        verify(request).setAttribute("errors", Map.of(
                "firstName", "Value is required",
                "lastName", "Value is required",
                "phone", "Value is required",
                "deliveryDate", "Value is required",
                "deliveryAddress", "Value is required",
                "paymentMethod", "Value is required"
        ));
        verify(requestDispatcher, times(1)).forward(request, response);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute("errors", Map.of());
        verify(request).setAttribute("order", order);
        verify(request).setAttribute("paymentMethods", paymentMethods);
        verify(requestDispatcher, times(1)).forward(request, response);
    }

}
