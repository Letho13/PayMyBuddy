package com.ocr.paymybuddy.PayMyBuddy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ocr.paymybuddy.PayMyBuddy.models.User;
import com.ocr.paymybuddy.PayMyBuddy.models.UserConnection;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserConnectionRepository;
import com.ocr.paymybuddy.PayMyBuddy.repositories.UserRepository;
import com.ocr.paymybuddy.PayMyBuddy.services.UserServiceImplementation;
import com.ocr.paymybuddy.PayMyBuddy.services.dto.UserRegistrationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserConnectionRepository userConnectionRepository;

    @InjectMocks
    private UserServiceImplementation userServiceImplementation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        UserRegistrationDto dto = new UserRegistrationDto("testUser", "test@example.com", "password");
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userServiceImplementation.save(dto);

        assertNotNull(savedUser);
        assertEquals("testUser", savedUser.getUsername());
        assertEquals("test@example.com", savedUser.getEmail());
    }

    @Test
    void testLoadUserByUsername_UserExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(userRepository.findUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userServiceImplementation.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findUserByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(org.springframework.security.core.userdetails.UsernameNotFoundException.class, () -> {
            userServiceImplementation.loadUserByUsername("notfound@example.com");
        });
    }

    @Test
    void testGetAll() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));

        List<User> users = userServiceImplementation.getAll();

        assertEquals(2, users.size());
    }

    @Test
    void testAddUserConnection_Success() {
        User user1 = new User();
        user1.setId(1);
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setId(2);
        user2.setEmail("user2@example.com");

        when(userRepository.findUserByEmail("user1@example.com")).thenReturn(Optional.of(user1));
        when(userRepository.findUserByEmail("user2@example.com")).thenReturn(Optional.of(user2));
        when(userConnectionRepository.save(any(UserConnection.class))).thenReturn(new UserConnection());

        assertDoesNotThrow(() -> userServiceImplementation.addUserConnection("user1@example.com", "user2@example.com"));
    }

    @Test
    void testAddUserConnection_UserNotFound() {
        when(userRepository.findUserByEmail("user1@example.com")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userServiceImplementation.addUserConnection("user1@example.com", "user2@example.com"));
    }
}
