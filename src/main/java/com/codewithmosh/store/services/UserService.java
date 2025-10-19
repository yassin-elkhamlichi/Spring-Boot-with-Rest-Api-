package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.AuthUserDto;
import com.codewithmosh.store.exception.InvalidPasswordException;
import com.codewithmosh.store.exception.UserNotFoundException;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    public String authUser(AuthUserDto authUserDto , UserRepository userRepository){
        var user = userRepository.findByEmail(authUserDto.getEmail());
        if(user == null)
            throw new UserNotFoundException();

        if(!passwordEncoder.matches(authUserDto.getPassword(), user.getPassword()))
            throw new InvalidPasswordException();

        return "Authenticated";
    }
}
