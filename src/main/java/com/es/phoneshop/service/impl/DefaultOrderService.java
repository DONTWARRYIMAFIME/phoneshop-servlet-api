package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.OrderService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    private OrderDao orderDao = ArrayListOrderDao.getInstance();
    private ProductDao productDao = ArrayListProductDao.getInstance();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final Lock writeLock = lock.writeLock();

    private DefaultOrderService() {}

    public static OrderService getInstance() {
        return Holder.orderService;
    }

    private static class Holder {
        private static final OrderService orderService = new DefaultOrderService();
    }

    @Override
    public Order createOrder(Cart cart) {
        writeLock.lock();
        try {
            Order order = new Order();

            Map<Long, CartItem> items = cart.getItems()
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> new CartItem(e.getValue())));
            
            order.setItems(items);
            order.setSubtotal(cart.getTotalPrice());
            order.setDeliveryPrice(calculateDeliveryPrice());
            order.setTotalPrice(order.getSubtotal().add(order.getDeliveryPrice()));

            return order;
        } finally {
            writeLock.unlock();
        }
    }

    private BigDecimal calculateDeliveryPrice() {
        return new BigDecimal(5);
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return Arrays.asList(PaymentMethod.values());
    }

    @Override
    public void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
        updateProductsStock(order);
    }

    private void updateProductsStock(Order order) {
        order.getItems().forEach((k, v) -> productDao.getProduct(k).ifPresent(p -> {
            p.setStock(p.getStock() - v.getQuantity());
            productDao.save(p);
        }));
    }

}
