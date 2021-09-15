package com.es.phoneshop.model.product.dao;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.sort.SortField;
import com.es.phoneshop.model.product.sort.SortOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private long maxId;
    private List<Product> products;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final Lock writeLock = lock.writeLock();
    private final Lock readLock = lock.readLock();

    private ArrayListProductDao() {
        products = new ArrayList<>();
    }

    public static ProductDao getInstance() {
        return Holder.productDao;
    }

    private static class Holder {
        private static final ProductDao productDao = new ArrayListProductDao();
    }

    @Override
    public Optional<Product> getProduct(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        readLock.lock();
        try {
            return products
                    .stream()
                    .filter(product -> Objects.equals(product.getId(), id))
                    .findAny();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<Product> findProducts() {
        readLock.lock();
        try {
            return products
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

    @Override
    public void save(Product product) {
        getProduct(product.getId())
                .ifPresent(oldProduct -> update(oldProduct, product));

        writeLock.lock();
        try {
            if (product.getId() == null) {
                product.setId(++maxId);
            } else if (product.getId() > maxId) {
                maxId = product.getId();
            }
            products.add(product);
        } finally {
            writeLock.unlock();
        }
    }

    private void update(Product oldProduct, Product product) {
        writeLock.lock();
        try {
            oldProduct.setCode(product.getCode());
            oldProduct.setDescription(product.getDescription());
            oldProduct.setPrice(product.getPrice());
            oldProduct.setCurrency(product.getCurrency());
            oldProduct.setStock(product.getStock());
            oldProduct.setImageUrl(product.getImageUrl());
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void delete(Long id) {
        writeLock.lock();
        try {
            products.removeIf(product -> Objects.equals(id, product.getId()));
        } finally {
            writeLock.unlock();
        }
    }

}
