package com.example.AddressBook.service.implimentation.auth;

import com.example.AddressBook.dto.AuthUserDTO;
import com.example.AddressBook.dto.LoginDTO;
import com.example.AddressBook.dto.LoginResponseDTO;
import com.example.AddressBook.model.AuthUser;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.repository.AuthUserRepository;
import com.example.AddressBook.repository.AddressBookRepository;
import com.example.AddressBook.service.IAuthenticationService;
import com.example.AddressBook.service.implimentation.EmailService;
import com.example.AddressBook.service.implimentation.MessagePublisher;
import com.example.AddressBook.util.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthenticationService implements IAuthenticationService {

    private final AuthUserRepository authUserRepository;
    private final AddressBookRepository addressBookRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final MessagePublisher messagePublisher;

    public AuthenticationService(MessagePublisher publisher,AuthUserRepository authUserRepository,
                                 AddressBookRepository addressBookRepository,
                                 BCryptPasswordEncoder passwordEncoder,
                                 JwtUtil jwtUtil,
                                 EmailService emailService) {
        this.authUserRepository = authUserRepository;
        this.addressBookRepository = addressBookRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.messagePublisher=publisher;
    }

    @Override
    public String resetPassword(String email, String currentPassword, String newPassword) {
        Optional<AuthUser> userOptional = authUserRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return "User not found with email: " + email;
        }

        AuthUser user = userOptional.get();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return "Current password is incorrect!";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        authUserRepository.save(user);

        return "Password reset successfully!";
    }

    @Override
    public String forgotPassword(String email, String newPass) {
        Optional<AuthUser> userOptional = authUserRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return "Sorry! We cannot find the user email: " + email;
        }

        AuthUser user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPass));
        authUserRepository.save(user);

        // Send confirmation email
        String subject = "Password Reset Confirmation";
        String message = "Hello " + user.getUsername() + ",\n\nYour password has been successfully updated.";
        emailService.sendEmail(user.getEmail(), subject, message, user.getEmail());

        return "Password has been changed successfully!";
    }

//    @Override
//    public String registerUser(AuthUserDTO userDTO) {
//        if (authUserRepository.findByEmail(userDTO.getEmail()).isPresent()) {
//            return "Email is already in use.";
//        }
//
//        AuthUser user = new AuthUser();
//        user.setUsername(userDTO.getUsername());
//        user.setEmail(userDTO.getEmail());
//        user.setRole(userDTO.getRole());
//        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
//
//        AuthUser savedUser = authUserRepository.save(user);
//
//        // Save associated address book entries
//        List<AddressBook> addressBookEntries = userDTO.getAddressBookEntries().stream().map(dto -> {
//            AddressBook addressBook = new AddressBook();
//            addressBook.setName(dto.getName());
//            addressBook.setAddress(dto.getAddress());
//            addressBook.setPhoneNumber(dto.getPhoneNumber());
//            addressBook.setUser(savedUser);
//            return addressBook;
//        }).collect(Collectors.toList());
//
//        addressBookRepository.saveAll(addressBookEntries);
//
//        // Send a welcome email
//        String subject = "Welcome to AddressBookApp!";
//        String message = "Hello " + user.getUsername() + ", welcome to our application!\n\n"
//                + "You can now securely manage your contacts.";
//
//        emailService.sendEmail(user.getEmail(), subject, message, user.getEmail());
//
//        return "User registered successfully!";
//    }
    @Override
    public String registerUser(AuthUserDTO userDTO) {
        if (authUserRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            return "Email is already in use.";
        }

        AuthUser user = new AuthUser();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        AuthUser savedUser = authUserRepository.save(user);

        // 🔥 Publish event for sending a welcome email
        String message = "New user registered: " + user.getEmail();
        messagePublisher.sendMessage("user.registration.queue", message);

        return "User registered successfully!";
    }
    @Override
    public LoginResponseDTO loginUser(LoginDTO loginDTO) {
        Optional<AuthUser> userOptional = authUserRepository.findByEmail(loginDTO.getEmail());

        if (userOptional.isEmpty()) {
            return new LoginResponseDTO("User not found!", null);
        }

        AuthUser user = userOptional.get();

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            return new LoginResponseDTO("Invalid email or password!", null);
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return new LoginResponseDTO("Login successful!", token);
    }
}
