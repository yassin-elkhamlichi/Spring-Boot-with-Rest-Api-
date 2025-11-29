package com.yassineproject.store.auth;

import com.yassineproject.store.users.User;
import com.yassineproject.store.users.UserRepository;
import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Data
@Service
public class AuthService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = (Long) authentication.getPrincipal();
        return userRepository.findById(id).orElse(null);
    }
}
