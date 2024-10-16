package com.cyclone.Repository.Interface;

import java.util.List;
import java.util.Optional;
import com.cyclone.Model.User;

public interface UserRepository {

    boolean saveUser(User user);  
    
    Optional<User> getUserById(int id);     
    
    Optional<User> getUserByEmail(String email);  
    
    List<User> getAllUsers();      
    
    boolean updateUser(User user); 
    
    boolean deleteUser(int id);    
}
