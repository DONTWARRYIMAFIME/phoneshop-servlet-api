package com.es.phoneshop.web.servlet.cart;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.EntityNotFoundException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;
import org.apache.http.client.utils.URIBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

public class PLPAddCartItemServlet extends HttpServlet  {

    private ProductDao productDao;
    private CartService cartService;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Product product = getProductByPathParamId(request);
        String quantityString = request.getParameter("quantity");

        Cart cart = cartService.getCart(request);
        try {
            Locale locale = request.getLocale();
            NumberFormat format = NumberFormat.getInstance(locale);
            int quantity = format.parse(quantityString).intValue();

            cartService.add(cart, product, quantity);
            doRedirect(Map.of("message", "Item added to cart successfully"), request, response);
        } catch (ParseException e) {
            doRedirect(Map.of("error", "Not a number", "productId", String.valueOf(product.getId()), "quantity", quantityString), request, response);
        } catch (IllegalArgumentException e) {
            doRedirect(Map.of("error", e.getMessage(), "productId", String.valueOf(product.getId()), "quantity", quantityString), request, response);
        }
    }

    private Product getProductByPathParamId(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        String[] pathParams = pathInfo.split("/");

        Long id = Long.parseLong(pathParams[pathParams.length - 1]);
        return productDao
                .find(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));
    }

    private void doRedirect(Map<String, String> newParams, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> oldParams = request.getParameterMap();

        try {
            URI uri = new URI(request.getContextPath() + "/products?");

            URIBuilder builder = new URIBuilder(uri);

            oldParams.forEach((k, v) -> builder.addParameter(k, v[0]));
            newParams.forEach(builder::addParameter);

            URI finalUri = builder.build();

            response.sendRedirect(finalUri.toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

}
