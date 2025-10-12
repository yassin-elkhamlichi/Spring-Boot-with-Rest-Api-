package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(@Email(message = "Email is invalid") String email);
}
