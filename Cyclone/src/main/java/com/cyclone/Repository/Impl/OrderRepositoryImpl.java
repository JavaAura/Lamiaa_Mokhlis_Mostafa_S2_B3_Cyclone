package com.cyclone.Repository.Impl;


import java.util.List;

import com.cyclone.DAO.Impl.OrderDAOImpl;
import com.cyclone.DAO.Interface.OrderDAO;
import com.cyclone.Model.Order;
import com.cyclone.Repository.Interface.OrderRepository;

public class OrderRepositoryImpl implements OrderRepository{

 private final OrderDAO orderDAO;
	 
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
}
