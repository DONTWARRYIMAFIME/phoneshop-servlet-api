package com.es.phoneshop.dao;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.search.SearchStructure;

import java.util.List;

public interface ProductDao extends GenericDao<Product> {
    List<Product> findProducts();
    List<Product> findProducts(SearchStructure searchStructure);
}
