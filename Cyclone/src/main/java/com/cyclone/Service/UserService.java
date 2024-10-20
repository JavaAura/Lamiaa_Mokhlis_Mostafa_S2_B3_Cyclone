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
			if (validPassword) {
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

	public Optional<List<User>> getAllUsers() {
		return Optional.ofNullable(userRepository.getAllUsers());
	}
	
	public Optional<List<User>> getAllUsersPaginated(int pageNumber, int pageSize) {
	    List<User> allUsers = userRepository.getAllUsers();

	    if (allUsers == null || allUsers.isEmpty()) {
	        return Optional.empty();
	    }

	    int start = pageNumber * pageSize;
	    int end = Math.min(start + pageSize, allUsers.size());

	    if (start >= allUsers.size() || start < 0) {
	        return Optional.empty();
	    }

	    return Optional.of(allUsers.subList(start, end));
	}

	public boolean updateUser(User user) {
		return userRepository.updateUser(user);
	}

	public boolean deleteUser(int id) {
		return userRepository.deleteUser(id);
	}
	
	public Optional<List<User>> searchUsersByName(String name) {
	    return userRepository.searchUsersByName(name);
	}

}
