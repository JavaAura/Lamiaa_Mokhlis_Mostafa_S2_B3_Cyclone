package com.cyclone.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import com.cyclone.Model.Order;
import com.cyclone.Model.Enum.OrderStatus;
import com.cyclone.Repository.Impl.OrderRepositoryImpl;

public class OrderService {

private final OrderRepositoryImpl orderRepository;


    public OrderService() {
        this.orderRepository =new OrderRepositoryImpl();
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
    
    public Optional<List<Order>> getOrdersByClient(int clientId) {
        List<Order> orders = orderRepository.getOrdersByClient(clientId);
        return Optional.ofNullable(orders); 
    }
    
    
      
}
