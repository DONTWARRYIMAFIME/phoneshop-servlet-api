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
import java.util.function.Consumer;
import java.util.function.Function;

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
        SearchStructure searchStructure = new SearchStructure();

        setRequiredParameter(request, "query", String::toString, searchStructure::setQuery);
        setRequiredParameter(request, "sort", SortField::safeValueOf, searchStructure::setSortField);
        setRequiredParameter(request, "order", SortOrder::safeValueOf, searchStructure::setSortOrder);

        request.setAttribute("products", productDao.findProducts(searchStructure));
        request.setAttribute("viewed", viewedService.getRecentlyViewedHistory(request));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }

    private <T> void setRequiredParameter(HttpServletRequest request, String parameter, Function<String, T> parser, Consumer<T> setter) {
        String value = request.getParameter(parameter);
        try {
            T parsed = parser.apply(value);
            setter.accept(parsed);
        } catch (NullPointerException e) {
            setter.accept(null);
        }
    }

}
