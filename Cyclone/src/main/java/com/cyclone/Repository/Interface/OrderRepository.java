package com.cyclone.Repository.Interface;

import java.util.List;

import com.cyclone.Model.Order;

public interface OrderRepository {

	void addOrder(Order order);

	Order findOrderById(int id);

	List<Order> getAllOrders();

	void modifyOrder(Order order);

	void removeOrder(int id);

	List<Order> getOrdersByClient(int clientId);

	List<Order> getOrdersWithPagination(int offset, int pageSize);

	int getTotalOrderCount();
}
