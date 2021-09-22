package com.es.phoneshop.model.product.viewed;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.Deque;

public interface RecentlyViewedHistoryService {

    Deque<Product> getRecentlyViewedHistory(HttpServletRequest request);

    void addProduct(Deque<Product> viewed, Product product);

}
