package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.search.SearchMode;
import com.es.phoneshop.model.search.SearchStructure;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class AdvancedSearchPageServlet extends HttpServlet {

    private ProductDao productDao;

    @Override
    public void init() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SearchStructure searchStructure = new SearchStructure();
        Map<String, String> errors = new HashMap<>();

        setRequiredParameter(request, "query", errors, String::toString, searchStructure::setQuery);
        setRequiredParameter(request, "searchMode", errors, SearchMode::valueOf, searchStructure::setSearchMode);
        setRequiredParameter(request, "minPrice", errors, BigDecimal::new, searchStructure::setMinPrice);
        setRequiredParameter(request, "maxPrice", errors, BigDecimal::new, searchStructure::setMaxPrice);
        setRequiredParameter(request, "minStock", errors, Integer::parseInt, searchStructure::setMinStock);
        setRequiredParameter(request, "maxStock", errors, Integer::parseInt, searchStructure::setMaxStock);

        if (request.getParameter("seen") != null) {
            request.setAttribute("products", productDao.findProducts(searchStructure));
        }

        request.setAttribute("errors", errors);
        request.setAttribute("searchModes", SearchMode.values());
        request.getRequestDispatcher("WEB-INF/pages/search.jsp").forward(request, response);
    }

    private <T> void setRequiredParameter(HttpServletRequest request, String parameter, Map<String, String> errors, Function<String, T> parser, Consumer<T> setter) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            setter.accept(null);
        } else {
            try {
                T parsed = parser.apply(value);
                setter.accept(parsed);
            } catch (RuntimeException e) {
                errors.put(parameter, "Invalid value");
            }
        }
    }

}