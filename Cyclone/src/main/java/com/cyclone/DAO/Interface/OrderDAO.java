package com.cyclone.DAO.Interface;

import java.util.List;

import com.cyclone.Model.Order;

public interface OrderDAO {


    Order createOrder(Order order);

    Order getOrderById(int id);

    List<Order> getAllOrders();

    Order updateOrder(Order order);

    void deleteOrder(int id);
}
