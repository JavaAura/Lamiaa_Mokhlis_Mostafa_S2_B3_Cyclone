package com.cyclone.Util;

import com.cyclone.Model.Admin;
import com.cyclone.Model.Enum.Role;
import com.cyclone.Service.UserService;

public class Main {

    public static void main(String[] args) {
        UserService userService = new UserService();

        // Set fake data for the Admin
        Admin admin = new Admin();
        admin.setFirstName("John");
        admin.setLastName("Doe");
        admin.setEmail("fsfd@exmaple.com");
        admin.setPassword(PasswordUtils.hashPassword("pass111"));
        admin.setRole(Role.ADMIN);
        admin.setAccessLevel("1");

        // Save the Admin to the database
        try {
            userService.createUser(admin);
            System.out.println("Admin user created successfully.");
        } catch (Exception e) {
            System.out.println("Error while creating admin user: " + e.getMessage());
        }
    }
}
