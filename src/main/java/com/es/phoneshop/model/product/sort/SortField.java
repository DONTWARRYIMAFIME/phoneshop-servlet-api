package com.es.phoneshop.model.product.sort;

import java.util.stream.Stream;

public enum SortField {
    DESCRIPTION,
    PRICE;

    public static SortField getEnum(String field) {
        return Stream
                .of(values())
                .filter(value -> value.toString().equalsIgnoreCase(field))
                .findAny()
                .orElse(null);
    }

}
