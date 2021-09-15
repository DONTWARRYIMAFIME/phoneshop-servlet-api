package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Product {
    private Long id;
    private String code;
    private String description;
    private int stock;
    private String imageUrl;

    private final SortedSet<PriceHistoryEntry> histories = new TreeSet<>(Comparator.comparing(PriceHistoryEntry::getDate).reversed());

    public Product() {}

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

}