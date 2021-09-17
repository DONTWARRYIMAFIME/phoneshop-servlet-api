package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.exception.IllegalProductQuantityException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final Lock writeLock = lock.writeLock();

    private DefaultCartService() {}

    public static CartService getInstance() {
        return Holder.cartService;
    }

    private static class Holder {
        private static final CartService cartService = new DefaultCartService();
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        lock.writeLock().lock();
        try {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void add(Cart cart, Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalProductQuantityException();
        }

        writeLock.lock();
        try {
            Long id = product.getId();

            CartItem cartItem = cart.getItems().get(id);
            int inCartQuantity = cartItem != null ? cartItem.getQuantity() : 0;
            int requestQuantity = inCartQuantity + quantity;

            if (requestQuantity > product.getStock()) {
                throw new OutOfStockException(requestQuantity, product.getStock());
            }

            cart.getItems().put(id, new CartItem(product, requestQuantity));
        } finally {
            writeLock.unlock();
        }

    }

}