package com.cyclone.Model;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.cyclone.Model.Enum.Role;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    @Column(nullable = false, length = 100)
    private String lastName;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(nullable = false, length = 150)
    private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;
	
	@OneToMany(mappedBy = "client" , cascade = {CascadeType.PERSIST , CascadeType.MERGE})
	private List<Order> orders;
	
	public User() {
    }

    public User(int id, String firstName, String lastName, String email, String password, Role role, List<Order> orders) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.orders = orders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
	
}
