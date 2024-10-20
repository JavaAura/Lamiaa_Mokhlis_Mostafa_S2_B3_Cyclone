package com.cyclone.Repository.Impl;

import java.util.List;
import java.util.Optional;

import com.cyclone.DAO.Impl.UserDAOImpl;
import com.cyclone.DAO.Interface.UserDAO;
import com.cyclone.Model.User;
import com.cyclone.Repository.Interface.UserRepository;

public class UserRepositoryImpl implements UserRepository {

    private UserDAO userDAO;

    public UserRepositoryImpl() {
        this.userDAO = new UserDAOImpl();
    }

    @Override
    public boolean saveUser(User user) {
        return userDAO.saveUser(user);
    }

    @Override
    public Optional<User> getUserById(int id) {
        User user = userDAO.getUserById(id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        User user = userDAO.getUserByEmail(email);
        return Optional.ofNullable(user); 
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers(); 
    }

    @Override
    public boolean updateUser(User user) {
        return userDAO.updateUser(user); 
    }

    @Override
    public boolean deleteUser(int id) {
        return userDAO.deleteUser(id);
    }
    
    @Override
    public Optional<List<User>> searchUsersByName(String name){
    	return Optional.of(userDAO.searchUsersByName(name));
    }
}
