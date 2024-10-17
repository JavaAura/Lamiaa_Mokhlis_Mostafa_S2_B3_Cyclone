package com.cyclone.Service;

import java.util.List;
import java.util.Optional;

import com.cyclone.Model.Order;
import com.cyclone.Repository.Interface.OrderRepository;

public class OrderService {

private final OrderRepository orderRepository;
    
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    public Optional<Order> findOrderById(int id) {
        return Optional.ofNullable(orderRepository.findOrderById(id));
    }

    public List<Order> getAllOrders() {
        return orderRepository.getAllOrders();
    }

    public void modifyOrder(Order order) {
        orderRepository.modifyOrder(order);
    }

    public void removeOrder(int id) {
        orderRepository.removeOrder(id);
    }
}
