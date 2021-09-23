package com.es.phoneshop.model.cart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Cart implements Serializable {

    private final Map<Long, CartItem> items = new LinkedHashMap<>();

    private int totalQuantity;
    private BigDecimal totalPrice;

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

    public void removeItem(Long id) {
        items.remove(id);
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
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