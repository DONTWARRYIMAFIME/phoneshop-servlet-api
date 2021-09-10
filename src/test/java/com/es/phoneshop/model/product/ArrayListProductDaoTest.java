package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;
import com.es.phoneshop.model.product.sort.SortField;
import com.es.phoneshop.model.product.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {

    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
    }

    @Test
    public void testFindOutOfStockProducts() {
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Product product4 = new Product();
        Product product5 = new Product();

        product3.setStock(0);
        product4.setStock(0);

        productDao
                .findProducts()
                .addAll(List.of(product1, product2, product3, product4, product5));

        List<Product> outOfStockProducts = productDao
                .findProducts()
                .stream()
                .filter((p) -> p.getStock() == 0)
                .collect(Collectors.toList());

        assertEquals(0, outOfStockProducts.size());
    }

    @Test
    public void testFindProductWithOutPrice() {
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Product product4 = new Product();
        Product product5 = new Product();

        productDao
                .findProducts()
                .addAll(List.of(product1, product2, product3, product4, product5));

        List<Product> productsWithOutPrice = productDao
                .findProducts()
                .stream()
                .filter((p) -> p.getPrice() == null)
                .collect(Collectors.toList());

        assertEquals(0, productsWithOutPrice.size());
    }

    @Test
    public void testFindProductWithCorrectId() {
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Product product4 = new Product();
        Product product5 = new Product();

        product1.setId(101L);
        product2.setId(102L);
        product3.setId(103L);
        product4.setId(104L);
        product5.setId(105L);

        productDao
                .findProducts()
                .addAll(List.of(product1, product2, product3, product4, product5));

        assertNotNull(productDao.getProduct(101L));
        assertNotNull(productDao.getProduct(102L));
        assertNotNull(productDao.getProduct(103L));
        assertNotNull(productDao.getProduct(104L));
        assertNotNull(productDao.getProduct(105L));
    }

    @Test
    public void testFindProductWithIncorrectId() {
        Long id  = -15L;
        assertEquals(Optional.empty(), productDao.getProduct(id));
    }

    @Test
    public void testSaveNewProductWithId() {
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Product product4 = new Product();
        Product product5 = new Product();

        product1.setId(101L);
        product2.setId(102L);
        product3.setId(103L);
        product4.setId(104L);
        product5.setId(105L);

        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);
        productDao.save(product4);
        productDao.save(product5);

        assertNotNull(productDao.getProduct(101L).orElse(null));
        assertNotNull(productDao.getProduct(102L).orElse(null));
        assertNotNull(productDao.getProduct(103L).orElse(null));
        assertNotNull(productDao.getProduct(104L).orElse(null));
        assertNotNull(productDao.getProduct(105L).orElse(null));
    }

    @Test
    public void testSaveNewProductWithOutId() {
        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Product product4 = new Product();
        Product product5 = new Product();

        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);
        productDao.save(product4);
        productDao.save(product5);

        assertTrue(product1.getId() > 0);
        assertTrue(product2.getId() > 0);
        assertTrue(product3.getId() > 0);
        assertTrue(product4.getId() > 0);
        assertTrue(product5.getId() > 0);
    }

    @Test
    public void testUpdateProduct() {
        Product oldProduct = new Product();
        oldProduct.setId(100L);
        oldProduct.setDescription("Old desc");

        productDao.save(oldProduct);

        Product newProduct = new Product();
        newProduct.setId(100L);
        newProduct.setDescription("Updated desc");

        productDao.save(newProduct);

        Product updatedProduct = productDao
                .getProduct(100L)
                .orElse(null);

        assertEquals("Updated desc", updatedProduct.getDescription());
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product();
        product.setId(100L);

        productDao.save(product);

        assertNotNull(productDao.getProduct(100L).orElse(null));

        productDao.delete(100L);

        assertNull(productDao.getProduct(100L).orElse(null));
    }

    @Test
    public void testDescriptionAscSort() {
        Product product1 = new Product(null, "Samsung", BigDecimal.valueOf(100), null, 5, null);
        Product product2 = new Product(null, "Xiaomi", BigDecimal.valueOf(100), null, 5, null);
        Product product3 = new Product(null, "Apple", BigDecimal.valueOf(100), null, 5, null);

        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);

        List<Product> products = productDao
                .findProducts(null, SortField.DESCRIPTION, SortOrder.ASC);

        assertEquals("Apple", products.get(0).getDescription());
        assertEquals("Samsung", products.get(1).getDescription());
        assertEquals("Xiaomi", products.get(2).getDescription());
    }

    @Test
    public void testPriceDescSort() {
        Product product1 = new Product(null, "Samsung", BigDecimal.valueOf(150), null, 5, null);
        Product product2 = new Product(null, "Xiaomi", BigDecimal.valueOf(320), null, 5, null);
        Product product3 = new Product(null, "Apple", BigDecimal.valueOf(100), null, 5, null);

        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);

        List<Product> products = productDao
                .findProducts(null, SortField.PRICE, SortOrder.DESC);

        assertEquals(BigDecimal.valueOf(320), products.get(0).getPrice());
        assertEquals(BigDecimal.valueOf(150), products.get(1).getPrice());
        assertEquals(BigDecimal.valueOf(100), products.get(2).getPrice());
    }

    @Test
    public void testQuery() {
        String query = "s 20";

        Product product1 = new Product(101L, null, "Samsung S", BigDecimal.valueOf(150), null, 5, null);
        Product product2 = new Product(102L,null, "Samsung", BigDecimal.valueOf(320), null, 5, null);
        Product product3 = new Product(103L,null, "Samsung S 20", BigDecimal.valueOf(100), null, 5, null);
        Product product4 = new Product(104L,null, "Iphone X", BigDecimal.valueOf(100), null, 5, null);
        Product product5 = new Product(105L,null, "Xiaomi MI 6", BigDecimal.valueOf(100), null, 5, null);
        Product product6 = new Product(106L,null, "LG G3", BigDecimal.valueOf(100), null, 5, null);

        productDao.save(product1);
        productDao.save(product2);
        productDao.save(product3);
        productDao.save(product4);
        productDao.save(product5);
        productDao.save(product6);

        List<Product> products = productDao
                .findProducts(query, null, null);

        assertEquals(3, products.size());

        assertEquals(product3, products.get(0));
        assertEquals(product1, products.get(1));
        assertEquals(product2, products.get(2));
    }


}
