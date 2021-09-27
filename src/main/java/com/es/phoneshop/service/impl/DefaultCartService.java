package com.es.phoneshop.service.impl;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
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
        writeLock.lock();
        try {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                cart = new Cart();
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart);
            }
            return cart;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void add(Cart cart, Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be more than 0");
        }

        writeLock.lock();
        try {
            Long id = product.getId();

            int inCartQuantity = Optional
                    .ofNullable(cart.getItems().get(id))
                    .map(CartItem::getQuantity)
                    .orElse(0);

            int requestQuantity = inCartQuantity + quantity;
            if (requestQuantity > product.getStock()) {
                throw new OutOfStockException(quantity, product.getStock() - inCartQuantity);
            }

            cart.addItem(id, new CartItem(product, requestQuantity));
        } finally {
            writeLock.unlock();
        }

    }

}