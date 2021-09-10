package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class PriceHistory {
    private final LocalDate date;
    private final BigDecimal price;
    private final Currency currency;

    public PriceHistory(LocalDate date, BigDecimal price, Currency currency) {
        this.date = date;
        this.price = price;
        this.currency = currency;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }

}
