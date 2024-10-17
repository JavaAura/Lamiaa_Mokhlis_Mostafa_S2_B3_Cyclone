package com.cyclone.DAO.Impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cyclone.DAO.Interface.OrderDAO;
import com.cyclone.Model.Order;

public class OrderDAOImpl implements OrderDAO{

	private EntityManager entityManager;

    @Override
    public Order createOrder(Order order) {
        entityManager.persist(order);
        return order;
    }
    
    @Override
    public Order getOrderById(int id) {
        return entityManager.find(Order.class, id);
    }

    @Override
    public List<Order> getAllOrders() {
    	  try {
    	        List<Order> orders = entityManager.createQuery("SELECT o FROM Order o", Order.class).getResultList();
    	        System.out.println("Fetched orders: " + orders.size());
    	        return orders;
    	    } catch (Exception e) {
    	        System.out.println("Error fetching orders: " + e.getMessage());
    	        e.printStackTrace();
    	        return null;
    	    }
    }

    @Override
    public Order updateOrder(Order order) {
        return entityManager.merge(order);
    }

    @Override
    public void deleteOrder(int id) {
        Order order = entityManager.find(Order.class, id);
        if (order != null) {
            entityManager.remove(order);
        }
    }
    
    
}
