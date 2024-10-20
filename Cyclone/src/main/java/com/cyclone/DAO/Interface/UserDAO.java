package com.cyclone.DAO.Interface;

import java.util.List;

import com.cyclone.Model.User;

public interface UserDAO {
	boolean saveUser(User user);
    User getUserById(int id);
    User getUserByEmail(String User);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(int id);
    List<User> searchUsersByName(String name);
}
