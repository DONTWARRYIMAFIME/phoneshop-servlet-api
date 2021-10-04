package com.es.phoneshop.model.order;

import java.util.stream.Stream;

public enum PaymentMethod {
    CASH,
    CREDIT_CARD;

    public static PaymentMethod safeValueOf(String field) {
        return Stream
                .of(values())
                .filter(value -> value.toString().equalsIgnoreCase(field))
                .findAny()
                .orElse(null);
    }
}
