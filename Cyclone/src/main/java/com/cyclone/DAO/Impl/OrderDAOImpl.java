package com.cyclone.DAO.Impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cyclone.DAO.Interface.OrderDAO;
import com.cyclone.Model.Order;
import com.cyclone.Util.JPAUtil;

import  org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class OrderDAOImpl implements OrderDAO {

	private EntityManager entityManager;
	 private static final Logger logger = LoggerFactory.getLogger(OrderDAO.class);
	 
	 public OrderDAOImpl() {
			this.entityManager = JPAUtil.getEntityManager();
		}
	 
	 @Override
		public Order createOrder(Order order) {
			try {
				entityManager.getTransaction().begin();
				entityManager.persist(order);
				entityManager.getTransaction().commit();
				return order;
			} catch (Exception e) {
				if (entityManager.getTransaction().isActive()) {
					entityManager.getTransaction().rollback();
				}
				logger.error("Error creating order: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
	 
	@Override
	public Order getOrderById(int id) {
		return entityManager.find(Order.class, id);
	}

	@Override
	public List<Order> getAllOrders() {
		try {
			List<Order> orders = entityManager.createQuery("SELECT o FROM Order o", Order.class).getResultList();
			return orders;
		} catch (Exception e) {
			logger.error("Error fetching orders: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Order updateOrder(Order order) {
		try {
			entityManager.getTransaction().begin();
			Order updatedOrder = entityManager.merge(order);
			entityManager.getTransaction().commit();
			return updatedOrder;
		} catch (Exception e) {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			logger.error("Error updating order: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void deleteOrder(int id) {
		try {
			entityManager.getTransaction().begin();
			Order order = entityManager.find(Order.class, id);
			if (order != null) {
				entityManager.remove(order);
			}
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			if (entityManager.getTransaction().isActive()) {
				entityManager.getTransaction().rollback();
			}
			logger.error("Error deleting order: " + e.getMessage());
			e.printStackTrace();
		}
	}


}
