package com.es.phoneshop.dao;

import com.es.phoneshop.model.order.Order;

import java.util.Optional;

public interface OrderDao extends GenericDao<Order> {
    Optional<Order> getOrderBySecureId(String secureId);
}
