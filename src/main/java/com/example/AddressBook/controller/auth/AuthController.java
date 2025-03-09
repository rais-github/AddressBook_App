package com.example.AddressBook.controller.auth;
import com.example.AddressBook.dto.LoginDTO;
import com.example.AddressBook.dto.AuthUserDTO;
import com.example.AddressBook.dto.LoginResponseDTO;
import com.example.AddressBook.service.IAuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Controller", description = "Handles User Authentication")
public class AuthController {
    private IAuthenticationService authService;

    public AuthController(IAuthenticationService authService) {
        this.authService = authService;
    }

    @Operation(summary = "User Login", description = "Validates user credentials and returns a JWT token.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginDTO loginDto) {
        LoginResponseDTO response = authService.loginUser(loginDto);

        if (response.getToken() == null) {
            return ResponseEntity.status(401).body(response);
        }

        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Register a new user", description = "Saves user details and returns success message.")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthUserDTO userDto) {
        System.out.println("🚀 Register Endpoint Hit: " + userDto.getEmail());
        String response = authService.registerUser(userDto);
        return ResponseEntity.status(response.startsWith("User registered successfully") ? 201 : 400)
                .body(response);
    }
    @Operation(summary = "Forgot Password", description = "Allows users to reset their password by providing their email and a new password.")
    @PutMapping("/forgotPassword/{email}")
    public ResponseEntity<String> forgotPassword(@PathVariable String email, @RequestBody Map<String, String> requestBody) {
        String newPassword = requestBody.get("password");
        String response = authService.forgotPassword(email, newPassword);
        return response.startsWith("Password has been changed") ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @Operation(summary = "Reset Password", description = "Allows authenticated users to change their password by providing the current and new password.")
    @PutMapping("/resetPassword/{email}")
    public ResponseEntity<String> resetPassword(@PathVariable String email, @RequestParam String currentPassword, @RequestParam String newPassword) {
        String response = authService.resetPassword(email, currentPassword, newPassword);
        return response.equals("Password reset successfully!") ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

}
