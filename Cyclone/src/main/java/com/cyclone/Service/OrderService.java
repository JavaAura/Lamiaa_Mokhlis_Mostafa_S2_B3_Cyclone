package com.cyclone.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.cyclone.Model.Order;
import com.cyclone.Model.Enum.OrderStatus;
import com.cyclone.Repository.Impl.OrderRepositoryImpl;

public class OrderService {

private final OrderRepositoryImpl orderRepository;
private final Map<Integer, Order> orderMap=new HashMap<>();

    public OrderService() {
        this.orderRepository =new OrderRepositoryImpl();
    }

	 
  public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

  private void LoadOrderMap() {
      List<Order> allOrders = getAllOrders();
      for (Order order : allOrders) {
          orderMap.put(order.getId(), order);
      }
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
    
    public List<Order> getOrdersByClientLastName(String lastName) {
    	  List<Order> searchedOrders = orderMap.values().stream()
    		        .filter(order -> order.getClient().getLastName().equalsIgnoreCase(lastName))
    		        .collect(Collectors.toList()); 
    		    
    		    return searchedOrders;
    }
    
    public List<Order> getOrdersWithPagination(int offset, int pageSize) {
        return orderRepository.getOrdersWithPagination(offset, pageSize);
    }
    
    public int getTotalOrderCount() {
        return orderRepository.getTotalOrderCount();
    }
      
}
