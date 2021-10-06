package com.es.phoneshop.service;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;

public interface RecentlyViewedHistoryService {
    LinkedList<Product> getRecentlyViewedHistory(HttpServletRequest request);
    void addProduct(LinkedList<Product> viewed, Product product);
}
