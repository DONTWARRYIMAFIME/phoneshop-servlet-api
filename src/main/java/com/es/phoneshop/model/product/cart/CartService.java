package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.exception.IllegalProductQuantityException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.product.Product;

public interface CartService {

    Cart getCart();

    void add(Product product, int quantity) throws OutOfStockException, IllegalProductQuantityException;

}