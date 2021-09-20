package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.sort.SortField;
import com.es.phoneshop.model.product.sort.SortOrder;
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

    private void setupProduct(Product product, Long id, String description, BigDecimal price, int stock) {
        when(product.getId()).thenReturn(id);
        when(product.getDescription()).thenReturn(description);
        when(product.getPrice()).thenReturn(price);
        when(product.getStock()).thenReturn(stock);
    }

    @Before
    public void setup() {
        setupProduct(product1, 101L, "Samsung S", BigDecimal.valueOf(101L), 101);
        setupProduct(product2, 102L, "Samsung", BigDecimal.valueOf(102L), 102);
        setupProduct(product3, 103L, "Samsung S 20", BigDecimal.valueOf(103L), 103);
        setupProduct(product4, 104L, "Iphone X", BigDecimal.valueOf(104L), 104);
        setupProduct(product5, 105L, "Xiaomi MI 6", BigDecimal.valueOf(105L), 105);
        setupProduct(product6, 106L, "Out of stock phone", BigDecimal.valueOf(106L), 0);
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
        assertNotNull(productDao.getProduct(101L));
        assertNotNull(productDao.getProduct(102L));
        assertNotNull(productDao.getProduct(103L));
        assertNotNull(productDao.getProduct(104L));
        assertNotNull(productDao.getProduct(105L));
    }

    @Test
    public void testFindProductWithIncorrectId() {
        assertEquals(Optional.empty(), productDao.getProduct(-15L));
    }

    @Test
    public void testDescriptionAscSort() {
        List<Product> products = productDao
                .findProducts(null, SortField.DESCRIPTION, SortOrder.ASC);

        assertEquals("Iphone X", products.get(0).getDescription());
        assertEquals("Samsung", products.get(1).getDescription());
        assertEquals("Samsung S", products.get(2).getDescription());
        assertEquals("Samsung S 20", products.get(3).getDescription());
        assertEquals("Xiaomi MI 6", products.get(4).getDescription());
    }

    @Test
    public void testPriceDescSort() {
        List<Product> products = productDao
                .findProducts(null, SortField.PRICE, SortOrder.DESC);

        assertEquals(BigDecimal.valueOf(105), products.get(0).getPrice());
        assertEquals(BigDecimal.valueOf(104), products.get(1).getPrice());
        assertEquals(BigDecimal.valueOf(103), products.get(2).getPrice());
        assertEquals(BigDecimal.valueOf(102), products.get(3).getPrice());
        assertEquals(BigDecimal.valueOf(101), products.get(4).getPrice());
    }

    @Test
    public void testFilter() {
        String query = "s 20";
        List<Product> products = productDao
                .findProducts(query, null, null);

        System.out.println(products);

        assertEquals(3, products.size());

        assertEquals(product3, products.get(0));
        assertEquals(product1, products.get(1));
        assertEquals(product2, products.get(2));
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

    @Test
    public void testSaveNewProductWithId() {
        assertNull(productDao.getProduct(107L).orElse(null));
        productWithHistory.setId(107L);
        productDao.save(productWithHistory);
        assertNotNull(productDao.getProduct(107L).orElse(null));
    }

    @Test
    public void testSaveNewProductWithOutId() {
        productDao.save(productWithHistory);
        assertTrue(productWithHistory.getId() > 0);
    }

    @Test
    public void testUpdateProduct() {
        assertNull(productWithHistory.getDescription());
        productWithHistory.setDescription("Updated desc");
        assertEquals("Updated desc", productWithHistory.getDescription());
    }

    @Test
    public void testDeleteProduct() {
        assertNotNull(productDao.getProduct(101L).orElse(null));
        productDao.delete(101L);
        assertNull(productDao.getProduct(101L).orElse(null));
    }

}
