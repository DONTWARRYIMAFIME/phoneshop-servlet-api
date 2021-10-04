package com.es.phoneshop.service.impl;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
        writeLock.lock();
        try {
            int inCartQuantity = Optional
                    .ofNullable(cart.getItems().get(product.getId()))
                    .map(CartItem::getQuantity)
                    .orElse(0);

            tryToAddItemToCart(cart, product, quantity, inCartQuantity);
        } finally {
            writeLock.unlock();
        }

    }

    @Override
    public void update(Cart cart, Product product, int quantity) throws OutOfStockException {
        writeLock.lock();
        try {
            tryToAddItemToCart(cart, product, quantity, 0);
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void delete(Cart cart, Long id) {
        writeLock.lock();
        try {
            cart.removeItem(id);
            recalculateTotalQuantityAndPrice(cart);
        } finally {
            writeLock.unlock();
        }
    }

    private void tryToAddItemToCart(Cart cart, Product product, int quantity, int inCartQuantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be more than 0");
        }

        int requestQuantity = inCartQuantity + quantity;
        if (requestQuantity > product.getStock()) {
            throw new OutOfStockException(quantity, product.getStock() - inCartQuantity);
        }

        cart.addItem(product.getId(), new CartItem(product, requestQuantity));
        recalculateTotalQuantityAndPrice(cart);
    }

    private void recalculateTotalQuantityAndPrice(Cart cart) {
        int totalQuantity = cart
                .getItems()
                .values()
                .stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        BigDecimal totalPrice = cart
                .getItems()
                .values()
                .stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalQuantity(totalQuantity);
        cart.setTotalPrice(totalPrice);
    }

}