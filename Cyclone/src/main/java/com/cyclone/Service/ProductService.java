package com.cyclone.Service;

import java.util.List;
import java.util.Optional;

import com.cyclone.Model.Product;
import com.cyclone.Repository.Impl.ProductRepositoryImpl;
import com.cyclone.Repository.Interface.ProductRepository;

/**
 * Service class for managing product-related operations.
 * This class acts as an intermediary between the controller and the repository,
 * providing business logic for product management.
 */
public class ProductService {
    private ProductRepository productRepository;

    /**
     * Constructs a new ProductService with a ProductRepositoryImpl.
     */
    public ProductService() {
        this.productRepository = new ProductRepositoryImpl();
    }

    /**
     * Retrieves all products from the repository.
     *
     * @return A list of all products.
     */
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    /**
     * Adds a new product to the repository.
     *
     * @param product The product to be added.
     * @return true if the product was successfully added, false otherwise.
     */
    public boolean addProduct(Product product) {
        return productRepository.addProduct(product);
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return An Optional containing the product if found, or an empty Optional if not found.
     */
    public Optional<Product> getProductById(int id) {
        return productRepository.getProductById(id);
    }

    /**
     * Updates an existing product in the repository.
     *
     * @param product The product with updated information.
     * @return true if the product was successfully updated, false otherwise.
     */
    public boolean updateProduct(Product product) {
        return productRepository.updateProduct(product);
    }

    /**
     * Deletes a product from the repository by its ID.
     *
     * @param id The ID of the product to delete.
     * @return true if the product was successfully deleted, false otherwise.
     */
    public boolean deleteProduct(int id) {
        return productRepository.deleteProduct(id);
    }
}
