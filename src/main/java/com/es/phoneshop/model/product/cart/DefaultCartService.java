package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.exception.EntityNotFoundException;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.dao.ArrayListProductDao;
import com.es.phoneshop.model.product.dao.ProductDao;

public class DefaultCartService implements CartService {

    private Cart cart = new Cart();
    private final ProductDao productDao;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static CartService getInstance() {
        return Holder.cartService;
    }

    private static class Holder {
        private static final CartService cartService = new DefaultCartService();
    }

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void add(Long productId, int quantity) {
        Product product = productDao
                .getProduct(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product", productId));

        cart.getItems().add(new CartItem(product, quantity));
    }

}