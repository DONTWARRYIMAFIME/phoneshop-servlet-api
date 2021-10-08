package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.search.SearchStructure;
import com.es.phoneshop.model.search.SortField;
import com.es.phoneshop.model.search.SortOrder;
import com.es.phoneshop.service.RecentlyViewedHistoryService;
import com.es.phoneshop.service.impl.DefaultRecentlyViewedHistoryService;

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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        SearchStructure searchStructure = new SearchStructure(query, SortField.safeValueOf(sortField), SortOrder.safeValueOf(sortOrder));

        request.setAttribute("products", productDao.findProducts(searchStructure));
        request.setAttribute("viewed", viewedService.getRecentlyViewedHistory(request));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

}
