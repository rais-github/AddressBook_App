package com.example.AddressBook.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "auth_user")
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;  // Either "USER" or "ADMIN"

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AddressBook> addressBookEntries;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<AddressBook> getAddressBookEntries() {
        return addressBookEntries;
    }

    public void setAddressBookEntries(List<AddressBook> addressBookEntries) {
        this.addressBookEntries = addressBookEntries;
    }
}
