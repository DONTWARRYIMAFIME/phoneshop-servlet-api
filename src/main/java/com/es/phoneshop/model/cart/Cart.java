package com.es.phoneshop.model.cart;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Cart implements Serializable {

    private final Map<Long, CartItem> items = new LinkedHashMap<>();

    public Cart() {}

    public Cart(Map<Long, CartItem> items) {
        this.items.putAll(items);
    }

    public Map<Long, CartItem> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public void addItem(Long id, CartItem item) {
        items.put(id, item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(items, cart.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    @Override
    public String toString() {
        return items.values().toString();
    }
}