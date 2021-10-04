package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.sort.SortField;
import com.es.phoneshop.model.sort.SortOrder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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
    public Optional<Product> getProduct(Long id) {
        return super.find(id);
    }

    @Override
    public List<Product> findProducts() {
        readLock.lock();
        try {
            return super.findAll()
                    .stream()
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        List<Product> searchedProducts = findProducts();

        if (query != null && !query.isEmpty()) {
            searchedProducts = filterProducts(searchedProducts, query);
        }

        if (sortField != null && sortOrder != null) {
            searchedProducts = sortProducts(searchedProducts, sortField, sortOrder);
        }

        return searchedProducts;
    }

    private List<Product> filterProducts(List<Product> products, String query) {
        readLock.lock();
        try {
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
        } finally {
            readLock.unlock();
        }
    }

    private List<Product> sortProducts(List<Product> products, SortField sortField, SortOrder sortOrder) {
        readLock.lock();
        try {
            Comparator<Product> comparator = sortField.getComparator();

            if (sortOrder == SortOrder.DESC) {
                comparator = comparator.reversed();
            }

            return products
                    .stream()
                    .sorted(comparator)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

}
