package com.es.phoneshop.model.product.viewed;

import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultRecentlyViewedHistory implements RecentlyViewedHistoryService {
    private static final String VIEWED_SESSION_ATTRIBUTE = "viewed";
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final Lock writeLock = lock.writeLock();

    private DefaultRecentlyViewedHistory() {}

    public static RecentlyViewedHistoryService getInstance() {
        return Holder.viewed;
    }

    private static class Holder {
        private static final RecentlyViewedHistoryService viewed = new DefaultRecentlyViewedHistory();
    }

    @Override
    public RecentlyViewedHistory getRecentlyViewedHistory(HttpServletRequest request) {
        lock.writeLock().lock();
        try {
            RecentlyViewedHistory viewed = (RecentlyViewedHistory) request.getSession().getAttribute(VIEWED_SESSION_ATTRIBUTE);
            if (viewed == null) {
                request.getSession().setAttribute(VIEWED_SESSION_ATTRIBUTE, viewed = new RecentlyViewedHistory());
            }
            return viewed;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void add(RecentlyViewedHistory viewed, Product product) {
        writeLock.lock();
        try {
            viewed.addProduct(product);
        } finally {
            writeLock.unlock();
        }
    }

}

