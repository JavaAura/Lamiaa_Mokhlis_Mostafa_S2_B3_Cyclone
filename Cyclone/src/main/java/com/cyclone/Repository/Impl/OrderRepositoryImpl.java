package com.cyclone.Repository.Impl;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.cyclone.DAO.Impl.OrderDAOImpl;
import com.cyclone.DAO.Interface.OrderDAO;
import com.cyclone.Model.Order;
import com.cyclone.Repository.Interface.OrderRepository;

public class OrderRepositoryImpl implements OrderRepository{

 private final OrderDAO orderDAO;
 
	private EntityManager entityManager;
	 
	 public OrderRepositoryImpl() {
	        this.orderDAO = new OrderDAOImpl();
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
	    	try {
	            List<Order> orders = entityManager.createQuery("SELECT o FROM Order o WHERE o.client.id = :clientId", Order.class)
	                                               .setParameter("clientId", clientId)
	                                               .getResultList();
	            System.out.println("Executing query for client ID: " + clientId);

	            System.out.println("Fetched orders for client ID " + clientId + ": " + orders.size());
	            return orders;
	        } catch (Exception e) {
	            System.out.println("Error fetching orders for client ID " + clientId + ": " + e.getMessage());
	            e.printStackTrace();
	            return null;
	        }
	    }
	    
	    

}
