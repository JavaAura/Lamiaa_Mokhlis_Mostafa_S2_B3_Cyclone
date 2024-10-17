package com.cyclone.Repository.Impl;

import com.cyclone.DAO.Impl.ProductDAOImpl;
import com.cyclone.DAO.Interface.ProductDAO;
import com.cyclone.Model.Product;
import com.cyclone.Repository.Interface.ProductRepository;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {
    private ProductDAO productDAO;

    public ProductRepositoryImpl() {
        this.productDAO = new ProductDAOImpl();
    }

    @Override
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

}
