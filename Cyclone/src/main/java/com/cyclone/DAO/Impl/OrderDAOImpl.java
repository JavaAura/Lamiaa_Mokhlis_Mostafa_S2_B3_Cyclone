package com.cyclone.DAO.Impl;

import java.util.List;

import javax.persistence.EntityManager;

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
        return entityManager.createQuery("SELECT o FROM Order o", Order.class).getResultList();
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
