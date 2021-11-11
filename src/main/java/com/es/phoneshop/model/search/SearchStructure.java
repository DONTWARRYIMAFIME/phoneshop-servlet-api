package com.es.phoneshop.model.search;

import java.math.BigDecimal;
import java.util.Optional;

public class SearchStructure {
    private String query;
    private SortField sortField;
    private SortOrder sortOrder;
    private SearchMode searchMode;
    private String code;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private int minStock;
    private int maxStock;

    public SearchStructure() {}

    public SearchStructure(String query, SortField sortField, SortOrder sortOrder) {
        this.query = query;
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public SortField getSortField() {
        return sortField;
    }

    public void setSortField(SortField sortField) {
        this.sortField = sortField;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public SearchMode getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(SearchMode searchMode) {
        this.searchMode = searchMode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getMinStock() {
        return minStock;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = Optional.ofNullable(minStock).orElse(0);
    }

    public int getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(Integer maxStock) {
        this.maxStock = Optional.ofNullable(maxStock).orElse(0);
    }
}
