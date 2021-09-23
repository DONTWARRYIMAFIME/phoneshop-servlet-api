package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.EntityNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CartPageServlet extends HttpServlet {

    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        doForward(null, cart, request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");

        Cart cart = cartService.getCart(request);
        Locale locale = request.getLocale();

        Map<Long, String> errors = new HashMap<>();

        for (int i = 0; i < productIds.length; i++) {
            Product product = getProductById(productIds[i]);

            try {
                int quantity = parseQuantity(quantities[i], locale);
                cartService.update(cart, product, quantity);

            } catch (ParseException e) {
                errors.put(product.getId(), "Not a number");
            } catch (IllegalArgumentException e) {
                errors.put(product.getId(), e.getMessage());
            }
        }

        if (errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully");
        } else {
            doForward(errors, cart, request, response);
        }

    }

    private Product getProductById(String productId) {
        Long id = Long.parseLong(productId);
        return productDao
                .getProduct(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));
    }

    private int parseQuantity(String quantity, Locale locale) throws ParseException {
        NumberFormat formatter = NumberFormat.getInstance(locale);
        return formatter.parse(quantity).intValue();
    }

    private void doForward(Map<Long, String> errors, Cart cart, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("errors", errors);
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

}