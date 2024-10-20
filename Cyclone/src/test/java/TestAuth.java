import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cyclone.Model.User;
import com.cyclone.Service.UserService;
import com.cyclone.Util.PasswordUtils;
import com.cyclone.DAO.Interface.UserDAO;

class TestAuth {

	/*
	 * @Mock private UserDAO userDAO;
	 * 
	 * @InjectMocks private UserService userService;
	 * 
	 * @BeforeEach void setUp() { // Initialize mocks before each test
	 * MockitoAnnotations.openMocks(this); }
	 * 
	 * @Test void testLoginValidCredentials() { String email = "test@example.com";
	 * String password = "password123";
	 * 
	 * String hashedPassword = PasswordUtils.hashPassword(password);
	 * 
	 * User mockUser = new User(); mockUser.setEmail(email);
	 * mockUser.setPassword(hashedPassword);
	 * 
	 * when(userDAO.getUserByEmail(email)).thenReturn(mockUser);
	 * 
	 * Optional<User> result = userService.login(email, password);
	 * 
	 * assertTrue(result.isPresent(), "User should be found for valid credentials");
	 * assertEquals(email, result.get().getEmail(),
	 * "The returned user's email should match the input email"); }
	 * 
	 * @Test void testLoginInvalidCredentials() { String email =
	 * "wrong@example.com"; String password = "wrongpassword";
	 * 
	 * when(userDAO.getUserByEmail(email)).thenReturn(null);
	 * 
	 * Optional<User> result = userService.login(email, password);
	 * 
	 * assertFalse(result.isPresent(),
	 * "User should not be found for invalid credentials"); }
	 */
}
