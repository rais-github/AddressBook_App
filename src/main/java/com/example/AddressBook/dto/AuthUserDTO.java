package com.example.AddressBook.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.List;

public class AuthUserDTO {

    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "Role is required (USER or ADMIN)")
    @Pattern(regexp = "USER|ADMIN", message = "Role must be USER or ADMIN")
    private String role;

    private List<AddressBookDTO> addressBookEntries;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<AddressBookDTO> getAddressBookEntries() {
        return addressBookEntries;
    }

    public void setAddressBookEntries(List<AddressBookDTO> addressBookEntries) {
        this.addressBookEntries = addressBookEntries;
    }
}
