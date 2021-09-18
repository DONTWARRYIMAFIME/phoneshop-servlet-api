package com.es.phoneshop.model.product.viewed;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;

public interface RecentlyViewedHistoryService {

    RecentlyViewedHistory getRecentlyViewedHistory(HttpServletRequest request);

    void add(RecentlyViewedHistory viewed, Product product);

}
