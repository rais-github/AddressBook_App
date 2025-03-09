package com.example.AddressBook.repository;
import com.example.AddressBook.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface AuthUserRepository extends JpaRepository<AuthUser,Long>{
    Optional<AuthUser> findByEmail(String email);
}



