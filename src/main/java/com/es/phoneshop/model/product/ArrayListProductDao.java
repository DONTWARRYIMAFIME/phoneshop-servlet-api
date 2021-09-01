package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private static long MAX_ID;
    private static final List<Product> products = new ArrayList<>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final Lock writeLock = lock.writeLock();
    private final Lock readLock = lock.readLock();

    private static ArrayListProductDao productDao;

    static {
        fillProductList();
    }

    private ArrayListProductDao() {}

    private static void fillProductList() {
        Currency usd = Currency.getInstance("USD");
        products.add(new Product(++MAX_ID, "sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        products.add(new Product(++MAX_ID, "sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        products.add(new Product(++MAX_ID, "sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        products.add(new Product(++MAX_ID, "iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        products.add(new Product(++MAX_ID, "iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        products.add(new Product(++MAX_ID, "htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        products.add(new Product(++MAX_ID, "sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        products.add(new Product(++MAX_ID, "xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        products.add(new Product(++MAX_ID, "nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        products.add(new Product(++MAX_ID, "palmp", "Palm Pixi", new BigDecimal(170), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        products.add(new Product(++MAX_ID, "simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        products.add(new Product(++MAX_ID, "simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        products.add(new Product(++MAX_ID, "simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
    }

    public synchronized static ArrayListProductDao getInstance() {
        if (productDao == null) {
            productDao = new ArrayListProductDao();
        }

        return productDao;
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
                    .filter((product) -> Objects.equals(product.getId(), id))
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
                .filter((product) -> product.getPrice() != null)
                .filter((product) -> product.getStock() > 0)
                .collect(Collectors.toList());
        }
        finally { readLock.unlock(); }
    }

    @Override
    public void save(Product product) {
        getProduct(product.getId())
                .ifPresent((oldProduct) -> update(oldProduct, product));

        writeLock.lock();
        try {
            if (product.getId() == null) {
                product.setId(++MAX_ID);
            } else if (product.getId() > MAX_ID) {
                MAX_ID = product.getId();
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
        try{ products.removeIf((product) -> Objects.equals(id, product.getId())); }
        finally {writeLock.unlock();}
    }

}
