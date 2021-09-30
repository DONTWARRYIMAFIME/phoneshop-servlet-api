package com.es.phoneshop.model.product;

import com.es.phoneshop.model.Entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Currency;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Product extends Entity implements Serializable {
    private String code;
    private String description;
    private int stock;
    private String imageUrl;

    private final SortedSet<PriceHistoryEntry> histories = new TreeSet<>();

    public Product() {
        this.histories.add(new PriceHistoryEntry(LocalDate.now(), null, null));
    }

    public Product(String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this.code = code;
        this.description = description;
        this.stock = stock;
        this.imageUrl = imageUrl;

        this.histories.add(new PriceHistoryEntry(LocalDate.now(), price, currency));
    }

    public Product(Long id, String code, String description, BigDecimal price, Currency currency, int stock, String imageUrl) {
        this(code, description, price, currency, stock, imageUrl);
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return histories.first().getPrice();
    }

    public void setPrice(BigDecimal price) {
        this.histories.add(new PriceHistoryEntry(LocalDate.now(), price, getCurrency()));
    }

    public void setPrice(BigDecimal price, LocalDate date) {
        this.histories.add(new PriceHistoryEntry(date, price, getCurrency()));
    }

    public Currency getCurrency() {
        return histories.first().getCurrency();
    }

    public void setCurrency(Currency currency) {
        histories.first().setCurrency(currency);
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<PriceHistoryEntry> getHistories() {
        return Collections.unmodifiableSet(histories);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", stock=" + stock +
                '}';
    }

}