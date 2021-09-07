package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;
    private Currency usd;

    @Before
    public void setup() {
        productDao = ArrayListProductDao.getInstance();
        usd = Currency.getInstance("USD");
    }

    @Test
    public void testFindProductsNoResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testFindOutOfStockProducts() {
        List<Product> outOfStockProducts = productDao
                .findProducts()
                .stream()
                .filter((p) -> p.getStock() == 0)
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
        Long id  = 1L;
        assertNotNull(productDao.getProduct(id));
    }

    @Test
    public void testFindProductWithIncorrectId() {
        Long id  = -15L;
        assertEquals(Optional.empty(), productDao.getProduct(id));
    }

    @Test
    public void testSaveNewProductWithId() {
        Product product = new Product(
                28L,
                "phoneCode",
                "test phone",
                new BigDecimal(300),
                usd,
                40,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"
        );

        productDao.save(product);
        assertTrue(productDao.findProducts().contains(product));
    }

    @Test
    public void testSaveNewProductWithOutId() {
        Product product = new Product(
                "phoneCode",
                "test phone",
                new BigDecimal(300),
                usd,
                40,
                "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"
        );

        productDao.save(product);
        assertTrue(product.getId() > 0);
    }

    @Test
    public void testUpdateProduct() {
        Long id = 1L;
        String newDescription = "Updated description";

        productDao
                .getProduct(id)
                .ifPresent((p) -> {
                    p.setDescription(newDescription);
                    productDao.save(p);
                });

        Product updatedProduct = productDao.getProduct(id).get();
        assertEquals("Updated description", updatedProduct.getDescription());
    }

    @Test
    public void testDeleteProduct() {
        Long id = 1L;
        productDao.delete(id);

        assertEquals(Optional.empty(), productDao.getProduct(id));
    }


}
