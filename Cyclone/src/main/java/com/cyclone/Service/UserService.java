package com.cyclone.Service;

import com.cyclone.Model.User;
import com.cyclone.Repository.Impl.UserRepositoryImpl;
import com.cyclone.Repository.Interface.UserRepository;
import com.cyclone.Util.PasswordUtils;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService() {
        this.userRepository = new UserRepositoryImpl();
    }

    public Optional<User> login(String email, String password) {

        Optional<User> userOpt = userRepository.getUserByEmail(email);

        if (userOpt.isPresent()) {
        	boolean validPassword = PasswordUtils.checkPassword(password, userOpt.get().getPassword());
        	if(validPassword) {
        		return userOpt;
        	}
        }
        

        return Optional.empty();
    }

    public boolean createUser(User user) {
        return userRepository.saveUser(user);
    }

    public Optional<User> getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public boolean updateUser(User user) {
        return userRepository.updateUser(user);
    }

    public boolean deleteUser(int id) {
        return userRepository.deleteUser(id);
    }
}
