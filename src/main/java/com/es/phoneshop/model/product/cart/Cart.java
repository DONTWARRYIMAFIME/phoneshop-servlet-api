package com.es.phoneshop.model.product.cart;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private final List<CartItem> items = new ArrayList<>();

    public List<CartItem> getItems() {
        return items;
    }

}
