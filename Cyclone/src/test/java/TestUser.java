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
import com.cyclone.Repository.Interface.UserRepository;
import com.cyclone.Service.UserService;

class TestUser {

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
        User newUser = new User(2, "NewUser", "LastName", "newuser@example.com", "hashedPassword", null, null);

        when(userRepository.saveUser(newUser)).thenReturn(true);

        boolean result = userService.createUser(newUser);

        assertTrue(result);
        verify(userRepository, times(1)).saveUser(newUser);
    }

    @Test
    void testGetUserById() {
        User mockUser = new User(1, "FirstName", "LastName", "user@example.com", "hashedPassword", null, null);

        when(userRepository.getUserById(1)).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.getUserById(1);

        assertTrue(result.isPresent());
        assertEquals(mockUser, result.get());
    }

    @Test
    void testGetAllUsers() {
        List<User> mockUsers = Arrays.asList(
                new User(1, "User1", "LastName1", "user1@example.com", "password1", null, null),
                new User(2, "User2", "LastName2", "user2@example.com", "password2", null, null)
        );

        when(userRepository.getAllUsers()).thenReturn(mockUsers);

        Optional<List<User>> result = userService.getAllUsers();

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }

    @Test
    void testUpdateUser() {
        User updatedUser = new User(1, "UpdatedName", "LastName", "user@example.com", "updatedPassword", null, null);

        when(userRepository.updateUser(updatedUser)).thenReturn(true);

        boolean result = userService.updateUser(updatedUser);

        assertTrue(result);
        verify(userRepository, times(1)).updateUser(updatedUser);
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
        List<User> mockUsers = Arrays.asList(
                new User(1, "User1", "LastName1", "user1@example.com", "password1", null, null),
                new User(2, "User2", "LastName2", "user2@example.com", "password2", null, null)
        );

        when(userRepository.searchUsersByName("User")).thenReturn(Optional.of(mockUsers));

        Optional<List<User>> result = userService.searchUsersByName("User");

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
    }
}
