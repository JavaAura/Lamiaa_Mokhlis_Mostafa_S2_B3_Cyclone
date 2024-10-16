package com.cyclone.Util;

import com.cyclone.Model.User;
import com.cyclone.Model.Enum.Role;
import com.cyclone.Repository.Impl.UserRepositoryImpl;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String plainPassword = "userPassword123";
		String hashedPassword = PasswordUtils.hashPassword(plainPassword);

		User user = new User();
		user.setFirstName("admin");
		user.setLastName("cyclone");
		user.setEmail("admin@cyclone.com");
		user.setRole(Role.ADMIN);
		user.setPassword(hashedPassword);
		UserRepositoryImpl userRepository = new UserRepositoryImpl();
		userRepository.saveUser(user);
	}

}
