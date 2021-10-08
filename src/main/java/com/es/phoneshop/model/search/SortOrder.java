package com.es.phoneshop.model.search;

import java.util.stream.Stream;

public enum SortOrder {
    ASC,
    DESC;

    public static SortOrder safeValueOf(String order) {
        return Stream
                .of(values())
                .filter(value -> value.toString().equalsIgnoreCase(order))
                .findAny()
                .orElse(null);
    }

}
