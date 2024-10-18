
	package com.cyclone.DAO.Impl;

	import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
	import javax.persistence.EntityTransaction;
	import javax.persistence.TypedQuery;

	import com.cyclone.DAO.Interface.ProductDAO;
	import com.cyclone.Model.Product;
	import com.cyclone.Util.JPAUtil;

	import org.slf4j.Logger;
	import org.slf4j.LoggerFactory;



	/**
	 * Implementation of the ProductDAO interface.
	 * This class provides concrete implementations for CRUD operations on Product entities.
	 */
	public class ProductDAOImpl implements ProductDAO {
		

	    private final EntityManager entityManager;
	    
	    private static final Logger logger = LoggerFactory.getLogger(ProductDAOImpl.class);


	    /**
	     * Constructor for ProductDAOImpl.
	     * Initializes the EntityManager using JPAUtil.
	     */
	    public ProductDAOImpl() {
	        this.entityManager = JPAUtil.getEntityManager();
	    }

	    /**
	     * Retrieves all products from the database.
	     *
	     * @return A List of all Product entities, or null if an error occurs.
	     */
	    @Override
	    public List<Product> getAllProducts() {
	        try {
	            String sql = "SELECT p FROM Product p";
	            TypedQuery<Product> query = entityManager.createQuery(sql, Product.class);
	            List<Product> products = query.getResultList();
	            return products ;
	        } catch (Exception e) {
	            logger.error("Error retrieving all products", e);
	            return null;
	        }	
	    }
		/**
		 * Adds a new product to the database.
		 *
		 * @param product The Product entity to be added.
		 * @return true if the product was added successfully, false otherwise.
		 */
		@Override
		public boolean addProduct(Product product) {
			try {
				EntityTransaction transaction = entityManager.getTransaction();
				transaction.begin();
				entityManager.persist(product);
				transaction.commit();
				return true;	
			} catch (Exception e) {
				logger.error("Error adding product", e);
				return false;
			}
		}

		@Override
		public Optional<Product> getProductById(int id) {
			try {
				return Optional.ofNullable(entityManager.find(Product.class, id));
			} catch (Exception e) {
				logger.error("Error retrieving product by ID", e);
				return null;
			}
		}

		@Override
		public boolean updateProduct(Product product) {
			try {
				EntityTransaction transaction = entityManager.getTransaction();
				transaction.begin();
				entityManager.merge(product);
				transaction.commit();
				return true;
			} catch (Exception e) {
				logger.error("Error updating product", e);
				return false;
			}
		}

		
		


		


	 
	

}
