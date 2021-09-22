package com.es.phoneshop.service.impl;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.RecentlyViewedHistoryService;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultRecentlyViewedHistoryService implements RecentlyViewedHistoryService {
    private static final String VIEWED_SESSION_ATTRIBUTE = DefaultRecentlyViewedHistoryService.class.getName() + ".viewed";
    private static final int HISTORY_LENGTH = 3;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final Lock writeLock = lock.writeLock();

    private DefaultRecentlyViewedHistoryService() {}

    public static RecentlyViewedHistoryService getInstance() {
        return Holder.viewed;
    }

    private static class Holder {
        private static final RecentlyViewedHistoryService viewed = new DefaultRecentlyViewedHistoryService();
    }

    @Override
    public LinkedList<Product> getRecentlyViewedHistory(HttpServletRequest request) {
        writeLock.lock();
        try {
            LinkedList<Product> viewed = (LinkedList<Product>)request.getSession().getAttribute(VIEWED_SESSION_ATTRIBUTE);
            if (viewed == null) {
                viewed = new LinkedList<>();
                request.getSession().setAttribute(VIEWED_SESSION_ATTRIBUTE, viewed);
            }
            return viewed;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void addProduct(LinkedList<Product> viewed, Product product) {
        writeLock.lock();
        try {
            viewed.removeIf(p -> p.equals(product));

            if (viewed.size() == HISTORY_LENGTH) {
                viewed.pollLast();
            }

            viewed.addFirst(product);
        } finally {
            writeLock.unlock();
        }
    }

}

