package com.cyclone.DAO.Interface;

import java.util.List;
import java.util.Optional;

import com.cyclone.Model.Product;

public interface ProductDAO {
    /**
     * Retrieves all products from the database.
     *
     * @return A list of all Product entities.
     */
    List<Product> getAllProducts();
    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return An Optional containing the Product entity if found, or empty if not found or an error occurs.
     */
    Optional<Product> getProductById(int id);
    /**
     * Adds a new product to the database.
     *
     * @param product The Product entity to be added.
     * @return true if the product was added successfully, false otherwise.
     */
    boolean addProduct(Product product);
    /**
     * Updates an existing product in the database.
     *
     * @param product The Product entity to be updated.
     * @return true if the product was updated successfully, false otherwise.
     */
    boolean updateProduct(Product product);
    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     * @return true if the product was deleted successfully, false otherwise.
     */
    boolean deleteProduct(int id);

}
