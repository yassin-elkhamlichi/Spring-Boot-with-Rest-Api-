package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.AuthUserDto;
import com.codewithmosh.store.exception.InvalidPasswordException;
import com.codewithmosh.store.exception.UserNotFoundException;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("user not found"));
        return new User(user.getEmail(),
                user.getPassword(),
                Collections.emptyList());
    }
}
