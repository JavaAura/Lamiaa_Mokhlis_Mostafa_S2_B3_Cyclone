package com.cyclone.Repository.Interface;

import java.util.List;
import java.util.Optional;

import com.cyclone.Model.Product;

public interface ProductRepository {

    List<Product> getAllProducts();
    boolean addProduct(Product product);
    Optional<Product> getProductById(int id);
    boolean updateProduct(Product product);
}
