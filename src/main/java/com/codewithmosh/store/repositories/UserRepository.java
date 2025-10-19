package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(@Email(message = "Email is invalid") String email);


    User findByEmail(@NotBlank(message = "Email is required") @Email String email);
}
