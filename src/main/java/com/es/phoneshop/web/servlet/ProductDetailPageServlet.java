package com.es.phoneshop.web.servlet;

import com.es.phoneshop.exception.EntityNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.service.impl.DefaultRecentlyViewedHistoryService;
import com.es.phoneshop.service.RecentlyViewedHistoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Locale;

public class ProductDetailPageServlet extends HttpServlet {

    private ProductDao productDao;
    private CartService cartService;
    private RecentlyViewedHistoryService viewedService;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        viewedService = DefaultRecentlyViewedHistoryService.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Product product = getProductByPathParamId(request);
        LinkedList<Product> viewed = viewedService.getRecentlyViewedHistory(request);
        viewedService.addProduct(viewed, product);
        doForward(viewed, product, request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Product product = getProductByPathParamId(request);
        Cart cart = cartService.getCart(request);
        Deque<Product> viewed = viewedService.getRecentlyViewedHistory(request);

        String quantityString = request.getParameter("quantity");
        try {
            Locale locale = request.getLocale();
            NumberFormat format = NumberFormat.getInstance(locale);
            int quantity = format.parse(quantityString).intValue();

            cartService.add(cart, product, quantity);
            response.sendRedirect(request.getContextPath() + "/products/" + product.getId() + "?message=Added to cart successfully");
        } catch (ParseException e) {
            request.setAttribute("error", "Not a number");
            doForward(viewed, product, request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doForward(viewed, product, request, response);
        }

    }

    private Product getProductByPathParamId(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        String[] pathParams = pathInfo.split("/");

        Long id = Long.parseLong(pathParams[1]);
        return productDao
                .getProduct(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));
    }

    private void doForward(Deque<Product> viewed, Product product, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("viewed", viewed);
        request.setAttribute("product", product);
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

}
