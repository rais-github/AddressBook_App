package com.example.AddressBook.controller.auth;

import com.example.AddressBook.dto.LoginDTO;
import com.example.AddressBook.dto.AuthUserDTO;
import com.example.AddressBook.service.IAuthenticationService;
import com.example.AddressBook.service.implimentation.EmailService;
import com.example.AddressBook.service.implimentation.MessagePublisher;
import com.example.AddressBook.service.implimentation.auth.AuthenticationService;
import com.example.AddressBook.repository.AuthUserRepository;
import com.example.AddressBook.repository.AddressBookRepository;
import com.example.AddressBook.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    private AuthController authController;
    private IAuthenticationService service;

    // Mock Dependencies
    private AuthUserRepository authUserRepository;
    private AddressBookRepository addressBookRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private EmailService emailService;
    private MessagePublisher messagePublisher;

    @BeforeEach
    void setUp() {
        // Create a service instance with dummy dependencies
        service = new AuthenticationService(
                messagePublisher,
                authUserRepository,
                addressBookRepository,
                passwordEncoder,
                jwtUtil,
                emailService
        );
        authController = new AuthController(service);
    }

    @Test
    void loginUser() {
        LoginDTO loginDTO = new LoginDTO("john@example.com", "password123");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authController.loginUser(loginDTO);
        });

        assertEquals("Login failed", exception.getMessage());
    }

    @Test
    void registerUser() {
        AuthUserDTO userDTO = new AuthUserDTO("AliceDoe", "alice@example.com", "securePassword");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authController.registerUser(userDTO);
        });

        assertEquals("Registration failed", exception.getMessage());
    }

    @Test
    void forgotPassword() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authController.forgotPassword("john@example.com");
        });

        assertEquals("Forgot password failed", exception.getMessage());
    }

    @Test
    void resetPassword() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authController.resetPassword("randomToken", "newPassword");
        });

        assertEquals("Password reset failed", exception.getMessage());
    }
}
