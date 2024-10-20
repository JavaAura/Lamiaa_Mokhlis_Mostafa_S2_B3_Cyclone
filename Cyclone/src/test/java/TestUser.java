import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cyclone.Model.User;
import com.cyclone.Model.Enum.Role;
import com.cyclone.Model.Admin;
import com.cyclone.Model.Client;
import com.cyclone.Repository.Interface.UserRepository;
import com.cyclone.Service.UserService;

class UserTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        User newUser = new Client();
        newUser.setFirstName("NewUser");
        newUser.setLastName("LastName");
        newUser.setRole(Role.CLIENT);
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("hashedPassword");

        when(userRepository.saveUser(newUser)).thenReturn(true);

        boolean result = userService.createUser(newUser);

        assertTrue(result);
    }

    @Test
    void testGetUserById() {
        Optional<User> result = userService.getUserById(4);

        assertTrue(result.isPresent());
        assertEquals("mypakoxic@mailinator.com", result.get().getEmail());
    }

    @Test
    void testGetAllUsers() {
        User user1 = new Client();
        user1.setFirstName("User1");
        user1.setLastName("LastName1");
        user1.setRole(Role.CLIENT);
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");

        User user2 = new Admin();
        user2.setFirstName("User2");
        user2.setLastName("LastName2");
        user2.setRole(Role.ADMIN);
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");

        List<User> mockUsers = Arrays.asList(user1, user2);

        when(userRepository.getAllUsers()).thenReturn(mockUsers);

        Optional<List<User>> result = userService.getAllUsers();

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }

    @Test
    void testUpdateUser() {
        User updatedUser = new Client();
        updatedUser.setFirstName("UpdatedName");
        updatedUser.setLastName("LastName");
        updatedUser.setRole(Role.CLIENT);
        updatedUser.setEmail("user@example.com");
        updatedUser.setPassword("updatedPassword");

        when(userRepository.updateUser(updatedUser)).thenReturn(true);

        boolean result = userService.updateUser(updatedUser);

        assertTrue(result);
    }

    @Test
    void testDeleteUser() {
        when(userRepository.deleteUser(1)).thenReturn(true);

        boolean result = userService.deleteUser(1);

        assertTrue(result);
        verify(userRepository, times(1)).deleteUser(1);
    }

    @Test
    void testSearchUsersByName() {
        User user1 = new Client();
        user1.setFirstName("User1");
        user1.setLastName("LastName1");
        user1.setRole(Role.CLIENT);
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");

        User user2 = new Admin();
        user2.setFirstName("User2");
        user2.setLastName("LastName2");
        user2.setRole(Role.ADMIN);
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");

        List<User> mockUsers = Arrays.asList(user1, user2);

        when(userRepository.searchUsersByName("User")).thenReturn(Optional.of(mockUsers));

        Optional<List<User>> result = userService.searchUsersByName("User");

        assertTrue(result.isPresent());
        assertNotNull(result.get());
    }
}
