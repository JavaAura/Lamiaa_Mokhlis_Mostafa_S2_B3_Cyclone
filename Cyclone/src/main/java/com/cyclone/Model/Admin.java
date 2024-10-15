package com.cyclone.Model;

import java.util.List;

import javax.persistence.*;

import com.cyclone.Model.Enum.Role;

@Entity
@DiscriminatorValue("admin")
public class Admin extends User {
	
	@Column(length = 10)
	private String accessLevel;
	
    public Admin() {
        super();
    }

    public Admin(int id, String firstName, String lastName, String email, String password, Role role, List<Order> orders,
                 String accessLevel) {
        super(id, firstName, lastName, email, password, role, orders); 
        this.accessLevel = accessLevel;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }
}
