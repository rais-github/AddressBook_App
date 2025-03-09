package com.example.AddressBook.service;

import com.example.AddressBook.dto.AuthUserDTO;
import com.example.AddressBook.dto.LoginDTO;
import com.example.AddressBook.dto.LoginResponseDTO;

public interface IAuthenticationService {
    String registerUser(AuthUserDTO userDTO);
    LoginResponseDTO loginUser(LoginDTO loginDTO);
    String forgotPassword(String email,String newPass);
    String resetPassword(String email, String currentPassword, String newPassword);
}

