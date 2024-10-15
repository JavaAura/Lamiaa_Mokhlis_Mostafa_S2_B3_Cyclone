package com.cyclone.Model;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;

import com.cyclone.Model.Enum.Role;

@Entity
@DiscriminatorValue("client")
public class Client extends User {
	
    @Size(min = 10, max = 250, message = "Please enter a valid delivery adress")
    @Column(length = 250)
	private String deliveryAddress;
	
    @Column(length = 100)
	private String paymentMethod;
	
    public Client() {
        super();
    }

    public Client(int id, String firstName, String lastName, String email, String password, Role role, List<Order> orders,
                  String deliveryAddress, String paymentMethod) {
        super(id, firstName, lastName, email, password, role, orders);
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
