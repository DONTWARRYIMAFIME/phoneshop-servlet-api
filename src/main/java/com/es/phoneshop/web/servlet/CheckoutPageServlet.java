package com.es.phoneshop.web.servlet;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultOrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class CheckoutPageServlet extends HttpServlet {
    private OrderService orderService;
    private CartService cartService;

    @Override
    public void init() {
        orderService = DefaultOrderService.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.createOrder(cart);
        doForward(Map.of(), order, request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        Order order = orderService.createOrder(cart);

        Map<String, String> errors = new HashMap<>();

        setRequiredParameter(request, "firstName", errors, String::toString, order::setFirstName);
        setRequiredParameter(request, "lastName", errors, String::toString, order::setLastName);
        setRequiredParameter(request, "phone", errors, String::toString, order::setPhone);
        setRequiredParameter(request, "deliveryAddress", errors, String::toString, order::setDeliveryAddress);
        setRequiredParameter(request, "deliveryDate", errors, LocalDate::parse, order::setDeliveryDate);
        setRequiredParameter(request, "paymentMethod", errors, PaymentMethod::valueOf, order::setPaymentMethod);

        if (errors.isEmpty()) {
            orderService.placeOrder(order);
            cartService.clear(cart);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());
        } else {
            doForward(errors, order, request, response);
        }
    }

    private void doForward(Map<String, String> errors, Order order, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("errors", errors);
        request.setAttribute("order", order);
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    private <T> void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors, Function<String, T> parser, Consumer<T> setter) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "Value is required");
        } else {
            try {
                T parsed = parser.apply(value);
                setter.accept(parsed);
            } catch (RuntimeException ignore) {
                errors.put(parameter, "Invalid value");
            }
        }
    }

}