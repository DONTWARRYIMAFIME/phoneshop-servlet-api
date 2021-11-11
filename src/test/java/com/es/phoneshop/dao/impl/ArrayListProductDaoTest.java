package com.es.phoneshop.dao.impl;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.search.SearchMode;
import com.es.phoneshop.model.search.SearchStructure;
import com.es.phoneshop.model.search.SortField;
import com.es.phoneshop.model.search.SortOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArrayListProductDaoTest {

    @Mock
    private Product product1;
    @Mock
    private Product product2;
    @Mock
    private Product product3;
    @Mock
    private Product product4;
    @Mock
    private Product product5;
    @Mock
    private Product product6;
    @Spy
    private Product productWithHistory;
    @Spy
    private ArrayList<Product> products;

    @InjectMocks
    private ProductDao productDao = ArrayListProductDao.getInstance();

    private void setupProduct(Product product, Long id, String description, String code, BigDecimal price, int stock) {
        when(product.getId()).thenReturn(id);
        when(product.getDescription()).thenReturn(description);
        when(product.getCode()).thenReturn(code);
        when(product.getPrice()).thenReturn(price);
        when(product.getStock()).thenReturn(stock);
    }

    @Before
    public void setup() {
        setupProduct(product1, 101L, "Samsung S", "sms", BigDecimal.valueOf(101L), 101);
        setupProduct(product2, 102L, "Samsung", "sm", BigDecimal.valueOf(102L), 102);
        setupProduct(product3, 103L, "Samsung S 20", "sms20",BigDecimal.valueOf(103L), 103);
        setupProduct(product4, 104L, "Iphone X", "ipx", BigDecimal.valueOf(104L), 104);
        setupProduct(product5, 105L, "Xiaomi MI 6", "mi6", BigDecimal.valueOf(105L), 105);
        setupProduct(product6, 106L, "Out of stock phone", "none", BigDecimal.valueOf(106L), 0);
        products.addAll(List.of(product1, product2, product3, product4, product5, product6));
    }

    @Test
    public void testFindOutOfStockProducts() {
        List<Product> outOfStockProducts = productDao
                .findProducts()
                .stream()
                .filter(product -> product.getStock() == 0)
                .collect(Collectors.toList());

        assertEquals(0, outOfStockProducts.size());
    }

    @Test
    public void testFindProductWithOutPrice() {
        List<Product> productsWithOutPrice = productDao
                .findProducts()
                .stream()
                .filter((p) -> p.getPrice() == null)
                .collect(Collectors.toList());

        assertEquals(0, productsWithOutPrice.size());
    }

    @Test
    public void testFindProductWithCorrectId() {
        assertNotNull(productDao.find(101L));
        assertNotNull(productDao.find(102L));
        assertNotNull(productDao.find(103L));
        assertNotNull(productDao.find(104L));
        assertNotNull(productDao.find(105L));
    }

    @Test
    public void testFindProductWithIncorrectId() {
        assertEquals(Optional.empty(), productDao.find(-15L));
    }

    @Test
    public void testDescriptionAscSort() {
        SearchStructure searchStructure = new SearchStructure();
        searchStructure.setSortField(SortField.DESCRIPTION);
        searchStructure.setSortOrder(SortOrder.ASC);

        List<Product> products = productDao.findProducts(searchStructure);

        assertEquals("Iphone X", products.get(0).getDescription());
        assertEquals("Samsung", products.get(1).getDescription());
        assertEquals("Samsung S", products.get(2).getDescription());
        assertEquals("Samsung S 20", products.get(3).getDescription());
        assertEquals("Xiaomi MI 6", products.get(4).getDescription());
    }

    @Test
    public void testPriceDescSort() {
        SearchStructure searchStructure = new SearchStructure();
        searchStructure.setSortField(SortField.PRICE);
        searchStructure.setSortOrder(SortOrder.DESC);

        List<Product> products = productDao.findProducts(searchStructure);

        assertEquals(BigDecimal.valueOf(105), products.get(0).getPrice());
        assertEquals(BigDecimal.valueOf(104), products.get(1).getPrice());
        assertEquals(BigDecimal.valueOf(103), products.get(2).getPrice());
        assertEquals(BigDecimal.valueOf(102), products.get(3).getPrice());
        assertEquals(BigDecimal.valueOf(101), products.get(4).getPrice());
    }

    @Test
    public void testFilterByAnyWords() {
        SearchStructure searchStructure = new SearchStructure();
        searchStructure.setQuery("s 20");

        List<Product> products = productDao.findProducts(searchStructure);

        assertEquals(3, products.size());
        assertEquals(List.of(product3, product1, product2), products);
    }

    @Test
    public void testFilterByAllWords() {
        SearchStructure searchStructure = new SearchStructure();
        searchStructure.setQuery("Samsung");
        searchStructure.setSearchMode(SearchMode.ALL_WORD);

        List<Product> products = productDao.findProducts(searchStructure);

        assertEquals(1, products.size());
        assertEquals(List.of(product2), products);
    }

    @Test
    public void testFilterByCode() {
        SearchStructure searchStructure = new SearchStructure();
        searchStructure.setCode("sms");

        List<Product> products = productDao.findProducts(searchStructure);

        assertEquals(2, products.size());
        assertEquals(List.of(product1, product3), products);
    }

    @Test
    public void testFilterByMinPrice() {
        SearchStructure searchStructure = new SearchStructure();
        searchStructure.setMinPrice(BigDecimal.valueOf(104));

        List<Product> products = productDao.findProducts(searchStructure);

        assertEquals(2, products.size());
        assertEquals(List.of(product4, product5), products);
    }

    @Test
    public void testFilterByMaxPrice() {
        SearchStructure searchStructure = new SearchStructure();
        searchStructure.setMaxPrice(BigDecimal.valueOf(102));

        List<Product> products = productDao.findProducts(searchStructure);

        assertEquals(2, products.size());
        assertEquals(List.of(product1, product2), products);
    }

    @Test
    public void testFilterByMinStock() {
        SearchStructure searchStructure = new SearchStructure();
        searchStructure.setMinStock(104);

        List<Product> products = productDao.findProducts(searchStructure);

        assertEquals(2, products.size());
        assertEquals(List.of(product4, product5), products);
    }

    @Test
    public void testFilterByMaxStock() {
        SearchStructure searchStructure = new SearchStructure();
        searchStructure.setMaxStock(102);

        List<Product> products = productDao.findProducts(searchStructure);

        assertEquals(2, products.size());
        assertEquals(List.of(product1, product2), products);
    }

    @Test
    public void testPriceHistory() {
        assertEquals(1, productWithHistory.getHistories().size());
        Currency usd = Currency.getInstance("USD");
        productWithHistory.setCurrency(usd);

        int historyLength = 8;
        for (int i = 0; i < historyLength; i++) {
            productWithHistory.setPrice(BigDecimal.valueOf(101 + i), LocalDate.of(2019, 7, i + 1));
        }

        assertEquals(historyLength + 1, productWithHistory.getHistories().size());
    }

}