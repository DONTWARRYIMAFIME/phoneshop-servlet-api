package com.es.phoneshop.web;

import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.sort.SortField;
import com.es.phoneshop.model.product.sort.SortOrder;
import com.es.phoneshop.model.product.viewed.DefaultRecentlyViewedHistoryService;
import com.es.phoneshop.model.product.viewed.RecentlyViewedHistoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProductListPageServlet extends HttpServlet {

    private ProductDao productDao;
    private RecentlyViewedHistoryService viewedService;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
        viewedService = DefaultRecentlyViewedHistoryService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        request.setAttribute("products", productDao.findProducts(query, SortField.safeValueOf(sortField), SortOrder.safeValueOf(sortOrder)));
        request.setAttribute("viewed", viewedService.getRecentlyViewedHistory(request));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

}
