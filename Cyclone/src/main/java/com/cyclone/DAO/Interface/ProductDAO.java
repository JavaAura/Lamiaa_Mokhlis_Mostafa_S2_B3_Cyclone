package com.cyclone.DAO.Interface;

import java.util.List;
import java.util.Optional;

import com.cyclone.Model.Product;

public interface ProductDAO {

    List<Product> getAllProducts();
    Optional<Product> getProductById(int id);
    boolean addProduct(Product product);
    boolean updateProduct(Product product);

}
