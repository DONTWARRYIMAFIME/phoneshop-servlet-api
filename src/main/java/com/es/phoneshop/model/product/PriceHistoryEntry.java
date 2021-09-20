package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

public class PriceHistoryEntry {
    private final LocalDate date;
    /** null means LocalDateTime is no price because the product is outdated or new */
    private final BigDecimal price;
    /** can be null if the price is null */
    private Currency currency;

    public PriceHistoryEntry(LocalDate date, BigDecimal price, Currency currency) {
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

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "PriceHistoryEntry{" +
                "date=" + date +
                ", price=" + price +
                ", currency=" + currency +
                '}';
    }

}
