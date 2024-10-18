package com.cyclone.Service;

import java.util.List;
import java.util.Optional;

import com.cyclone.Model.Product;
import com.cyclone.Repository.Impl.ProductRepositoryImpl;
import com.cyclone.Repository.Interface.ProductRepository;

public class ProductService {
    private ProductRepository productRepository;

    public ProductService() {
        this.productRepository = new ProductRepositoryImpl();
    }

    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public boolean addProduct(Product product) {
        return productRepository.addProduct(product);
    }

    public Optional<Product> getProductById(int id) {
        return productRepository.getProductById(id);
    }

    public boolean updateProduct(Product product) {
        return productRepository.updateProduct(product);
    }

}
