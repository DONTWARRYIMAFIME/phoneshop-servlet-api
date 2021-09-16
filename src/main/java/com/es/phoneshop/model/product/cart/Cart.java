package com.es.phoneshop.model.product.cart;

import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {

    private final Map<Long, CartItem> items = new LinkedHashMap<>();

    public Map<Long, CartItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "items=" + items +
                '}';
    }
}
