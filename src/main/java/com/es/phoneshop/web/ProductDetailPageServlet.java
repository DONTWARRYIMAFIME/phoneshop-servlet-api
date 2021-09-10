package com.es.phoneshop.web;

import com.es.phoneshop.exception.EntityNotFoundException;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ProductDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductDetailPageServlet extends HttpServlet {

    private ProductDao productDao;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String[] pathParams = pathInfo.split("/");

        /*
         * Redirecting URL without product id (phoneshop-servlet-api/products/ -> phoneshop-servlet-api/products)
         */
        if (pathParams.length == 0) {
            request.getRequestDispatcher(request.getContextPath() + "/products").forward(request, response);
        } else {
            Long id = Long.parseLong(pathParams[1]);

            Product product = productDao
                    .getProduct(id)
                    .orElseThrow(() -> new EntityNotFoundException("Product", id));

            request.setAttribute("product", product);
            request.getRequestDispatcher("/WEB-INF/pages/product.jsp").forward(request, response);
        }
    }

}
