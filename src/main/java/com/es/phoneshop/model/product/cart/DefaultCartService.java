package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.exception.IllegalProductQuantityException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;

public class DefaultCartService implements CartService {

    private final Cart cart = new Cart();

    private DefaultCartService() {}

    public static CartService getInstance() {
        return Holder.cartService;
    }

    private static class Holder {
        private static final CartService cartService = new DefaultCartService();
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void add(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalProductQuantityException();
        }

        Long id = product.getId();

        CartItem cartItem = cart.getItems().get(id);
        int inCartQuantity = cartItem != null ? cartItem.getQuantity() : 0;
        int requestQuantity = inCartQuantity + quantity;

        if (requestQuantity > product.getStock()) {
            throw new OutOfStockException(requestQuantity, product.getStock());
        }

        cart.getItems().put(id, new CartItem(product, requestQuantity));
    }

}