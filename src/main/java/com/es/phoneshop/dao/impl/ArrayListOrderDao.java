package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.model.order.Order;

import java.util.Optional;

public class ArrayListOrderDao extends ArrayListGenericDao<Order> implements OrderDao {

    private ArrayListOrderDao() {}

    public static OrderDao getInstance() {
        return Holder.orderDao;
    }

    private static class Holder {
        private static final OrderDao orderDao = new ArrayListOrderDao();
    }

    @Override
    public Optional<Order> getOrderBySecureId(String secureId) {
        readLock.lock();
        try {
            return super.findAll()
                    .stream()
                    .filter(o -> o.getSecureId().equals(secureId))
                    .findAny();
        } finally {
            readLock.unlock();
        }
    }

}
