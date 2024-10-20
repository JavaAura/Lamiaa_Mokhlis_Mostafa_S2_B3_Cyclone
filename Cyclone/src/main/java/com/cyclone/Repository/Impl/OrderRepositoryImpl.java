package com.cyclone.Repository.Impl;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cyclone.DAO.Impl.OrderDAOImpl;
import com.cyclone.DAO.Interface.OrderDAO;
import com.cyclone.Model.Order;
import com.cyclone.Repository.Interface.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class OrderRepositoryImpl implements OrderRepository {

	private static final Logger logger = LoggerFactory.getLogger(OrderRepositoryImpl.class);
	private static final String PERSISTENCE_UNIT_NAME = "cycloneJPA";
	private final OrderDAO orderDAO;

	private EntityManager entityManager;
	private EntityManagerFactory emf;


	public OrderRepositoryImpl() {
		this.orderDAO = new OrderDAOImpl();
		initEntityManager();
	}

	private void initEntityManager() {
		try {
			emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
			entityManager = emf.createEntityManager();
			logger.info("EntityManager initialized successfully");
		} catch (Exception e) {
			logger.error("Failed to initialize EntityManager", e);
		}
	}
	
	@Override
	public void addOrder(Order order) {
		orderDAO.createOrder(order);
	}

	@Override
	public Order findOrderById(int id) {
		return orderDAO.getOrderById(id);
	}

	@Override
	public List<Order> getAllOrders() {
		return orderDAO.getAllOrders();
	}

	@Override
	public void modifyOrder(Order order) {
		orderDAO.updateOrder(order);
	}

	@Override
	public void removeOrder(int id) {
		orderDAO.deleteOrder(id);
	}

	@Override
	public List<Order> getOrdersByClient(int clientId) {
		logger.debug("Fetching orders for client ID: {}", clientId);
		if (entityManager == null) {
			logger.error("EntityManager is null. Ensure proper initialization.");
			throw new IllegalStateException("EntityManager is not initialized");
		}
		return entityManager.createQuery("SELECT o FROM Order o WHERE o.client.id = :clientId", Order.class)
				.setParameter("clientId", clientId)
				.getResultList();
	}

	@Override
	public List<Order> getOrdersWithPagination(int offset, int pageSize) {
		return entityManager.createQuery("SELECT o FROM Order o", Order.class).setFirstResult(offset)
				.setMaxResults(pageSize).getResultList();
	}

	@Override
	public int getTotalOrderCount() {
		try {
			Long count = entityManager.createQuery("SELECT COUNT(o) FROM Order o", Long.class).getSingleResult();
			return count.intValue();
		} catch (Exception e) {
			System.out.println("Error fetching total order count: " + e.getMessage());
			e.printStackTrace();
			return 0;
		}
	}
	
	public void close() {
		if (entityManager != null && entityManager.isOpen()) {
			entityManager.close();
		}
		if (emf != null && emf.isOpen()) {
			emf.close();
		}
		logger.info("EntityManager and EntityManagerFactory closed");
	}

}
