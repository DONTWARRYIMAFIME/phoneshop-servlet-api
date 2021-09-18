package com.es.phoneshop.model.product.viewed;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayDeque;
import java.util.Deque;

public class RecentlyViewedHistory {
    private static final int HISTORY_LENGTH = 3;
    private final Deque<Product> products = new ArrayDeque<>();

    public void addProduct(Product product) {
        products.removeIf(p -> p.equals(product));

        if (products.size() == HISTORY_LENGTH) {
            products.pollLast();
        }

        products.addFirst(product);
    }

    public Deque<Product> getProducts() {
        return products;
    }
}
