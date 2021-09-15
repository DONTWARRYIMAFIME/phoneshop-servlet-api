package com.es.phoneshop.model.product.sort;

import com.es.phoneshop.model.product.Product;

import java.util.Comparator;
import java.util.stream.Stream;

public enum SortField {
    DESCRIPTION(Comparator.comparing(Product::getDescription)),
    PRICE(Comparator.comparing(Product::getPrice));

    private final Comparator<Product> comparator;

    SortField(Comparator<Product> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Product> getComparator() {
        return comparator;
    }

    public static SortField safeValueOf(String field) {
        return Stream
                .of(values())
                .filter(value -> value.toString().equalsIgnoreCase(field))
                .findAny()
                .orElse(null);
    }

}
