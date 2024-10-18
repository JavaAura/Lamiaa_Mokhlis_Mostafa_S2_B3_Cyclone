package com.cyclone.Repository.Impl;

import com.cyclone.DAO.Impl.ProductDAOImpl;
import com.cyclone.DAO.Interface.ProductDAO;
import com.cyclone.Model.Product;
import com.cyclone.Repository.Interface.ProductRepository;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ProductRepository interface.
 * This class provides methods to interact with the product data in the database.
 */
public class ProductRepositoryImpl implements ProductRepository {
    private ProductDAO productDAO;

    /**
     * Constructs a new ProductRepositoryImpl.
     * Initializes the productDAO with a new instance of ProductDAOImpl.
     */
    public ProductRepositoryImpl() {
        this.productDAO = new ProductDAOImpl();
    }

    /**
     * Retrieves all products from the database.
     *
     * @return A list of all Product objects in the database.
     */
    @Override
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }
    
    /**
     * Adds a new product to the database.
     *
     * @param product The Product object to be added.
     * @return true if the product was successfully added, false otherwise.
     */
    @Override
    public boolean addProduct(Product product) {
        return productDAO.addProduct(product);
    }

    /**
     * Retrieves a product from the database by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return An Optional containing the Product if found, or an empty Optional if not found.
     */
    @Override
    public Optional<Product> getProductById(int id) {
        return productDAO.getProductById(id);
    }

    /**
     * Updates an existing product in the database.
     *
     * @param product The Product object with updated information.
     * @return true if the product was successfully updated, false otherwise.
     */
    @Override
    public boolean updateProduct(Product product) {
        return productDAO.updateProduct(product);
    }

    /**
     * Deletes a product from the database by its ID.
     *
     * @param id The ID of the product to delete.
     * @return true if the product was successfully deleted, false otherwise.
     */
    @Override
    public boolean deleteProduct(int id) {
        return productDAO.deleteProduct(id);
    }
}
