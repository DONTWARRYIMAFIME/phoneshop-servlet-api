package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.search.SearchMode;
import com.es.phoneshop.model.search.SearchStructure;
import com.es.phoneshop.model.search.SortField;
import com.es.phoneshop.model.search.SortOrder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArrayListProductDao extends ArrayListGenericDao<Product> implements ProductDao {

    private ArrayListProductDao() {}

    public static ProductDao getInstance() {
        return Holder.productDao;
    }

    private static class Holder {
        private static final ProductDao productDao = new ArrayListProductDao();
    }

    @Override
    public List<Product> findProducts() {
        return findProducts(null);
    }

    @Override
    public List<Product> findProducts(SearchStructure searchStructure) {
        readLock.lock();
        try {
            List<Product> searchedProducts = findValidProducts();

            if (searchStructure != null) {
                searchedProducts = filterByQuery(searchedProducts, searchStructure.getSearchMode(), searchStructure.getQuery());
                searchedProducts = sortProducts(searchedProducts, searchStructure.getSortField(), searchStructure.getSortOrder());
                searchedProducts = filterByCode(searchedProducts, searchStructure.getCode());
                searchedProducts = filterByMinPrice(searchedProducts, searchStructure.getMinPrice());
                searchedProducts = filterByMaxPrice(searchedProducts, searchStructure.getMaxPrice());
                searchedProducts = filterByMinStock(searchedProducts, searchStructure.getMinStock());
                searchedProducts = filterByMaxStock(searchedProducts, searchStructure.getMaxStock());
            }

            return searchedProducts;
        } finally {
            readLock.unlock();
        }
    }

    private List<Product> findValidProducts() {
        return super.findAll()
                .stream()
                .filter(product -> product.getPrice() != null)
                .filter(product -> product.getStock() > 0)
                .collect(Collectors.toList());
    }

    private List<Product> filterByQuery(List<Product> products, SearchMode searchMode, String query) {
        if (query == null) {
            return products;
        }

        if (searchMode == SearchMode.ALL_WORD) {
            return filterByAllWord(products, query);
        } else {
            return filterByAnyWords(products, query);
        }
    }

    private List<Product> filterByCode(List<Product> products, String code) {
        if (code == null) {
            return products;
        }

        return products
                .stream()
                .filter(product -> product.getCode().toUpperCase(Locale.ROOT).contains(code.toUpperCase(Locale.ROOT)))
                .collect(Collectors.toList());
    }

    private List<Product> filterByAllWord(List<Product> products, String query) {
        return products
                .stream()
                .filter(product -> query.equalsIgnoreCase(product.getDescription()))
                .collect(Collectors.toList());
    }

    private List<Product> filterByAnyWords(List<Product> products, String query) {
        String[] queries = query.toUpperCase(Locale.ROOT).split("\\s+");

        Function<Product, Long> countMatchesFunction = p -> Arrays
                .stream(queries)
                .filter(q -> p.getDescription().toUpperCase(Locale.ROOT).contains(q))
                .count();

        return products
                .stream()
                .filter(p -> countMatchesFunction.apply(p) != 0)
                .sorted(Comparator.comparing(countMatchesFunction).reversed())
                .collect(Collectors.toList());
    }

    private List<Product> sortProducts(List<Product> products, SortField sortField, SortOrder sortOrder) {
        if (sortField == null || sortOrder == null) {
            return products;
        }

        Comparator<Product> comparator = sortField.getComparator();

        if (sortOrder == SortOrder.DESC) {
            comparator = comparator.reversed();
        }

        return products
                .stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    private List<Product> filterByMinPrice(List<Product> products, BigDecimal minPrice) {
        if (minPrice == null) {
            return products;
        }

        return products
                .stream()
                .filter(product -> minPrice.compareTo(product.getPrice()) <= 0)
                .collect(Collectors.toList());
    }

    private List<Product> filterByMaxPrice(List<Product> products, BigDecimal maxPrice) {
        if (maxPrice == null) {
            return products;
        }

        return products
                .stream()
                .filter(product -> maxPrice.compareTo(product.getPrice()) >= 0)
                .collect(Collectors.toList());
    }

    private List<Product> filterByMinStock(List<Product> products, int minStock) {
        if (minStock == 0) {
            return products;
        }

        return products
                .stream()
                .filter(product -> product.getStock() >= minStock)
                .collect(Collectors.toList());
    }

    private List<Product> filterByMaxStock(List<Product> products, int maxStock) {
        if (maxStock == 0) {
            return products;
        }

        return products
                .stream()
                .filter(product -> product.getStock() <= maxStock)
                .collect(Collectors.toList());
    }

}

