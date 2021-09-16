package com.es.phoneshop.web;

import com.es.phoneshop.exception.EntityNotFoundException;
import com.es.phoneshop.exception.IllegalProductQuantityException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class ProductDetailPageServlet extends HttpServlet {

    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Product product = getProductByPathParamId(request);
        Cart cart = getCartFromSession(request);
        doForward(cart, product, request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Product product = getProductByPathParamId(request);
        Cart cart = getCartFromSession(request);

        String quantityString = request.getParameter("quantity");
        try {
            Locale locale = request.getLocale();
            NumberFormat format = NumberFormat.getInstance(locale);
            int quantity = format.parse(quantityString).intValue();

            cartService.add(cart, product, quantity);
            response.sendRedirect(request.getContextPath() + "/products/" + product.getId() + "?message=Added to cart successfully");
        } catch (ParseException e) {
            request.setAttribute("error", "Not a number");
            doForward(cart, product, request, response);
        } catch (IllegalProductQuantityException | OutOfStockException e) {
            request.setAttribute("error", e.getMessage());
            doForward(cart, product, request, response);
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

    private void doForward(Cart cart, Product product, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cart", cart);
        request.setAttribute("product", product);
        request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
    }

    private Cart getCartFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (Cart)session.getAttribute("cart");
    }

}
