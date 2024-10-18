package com.cyclone.Model;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.cyclone.Model.Enum.OrderStatus;

@Entity
@Table(name = "orders")
public class Order {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Order date cannot be null")
    @Column(nullable = false)
    private LocalDate orderDate;

    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private int quantity;

    @NotNull(message = "Order status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "clientID", nullable = false)
    @NotNull(message = "Client cannot be null")
    private User client;
	
	@ManyToMany
	@JoinTable(
			name = "order_product",
			joinColumns = @JoinColumn(name = "orderID", nullable = false),
			inverseJoinColumns = @JoinColumn(name = "ProductID", nullable = false)
			)
	private List<Product> products;
	
    public Order() {
    }

    public Order(int id, LocalDate orderDate, int quantity, OrderStatus status, User client, List<Product> products) {
        this.id = id;
        this.orderDate = orderDate;
        this.quantity = quantity;
        this.status = status;
        this.client = client;
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        this.products.add(product); 
    }
}
