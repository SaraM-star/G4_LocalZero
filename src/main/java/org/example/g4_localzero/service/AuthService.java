package org.example.g4_localzero.service;

import lombok.RequiredArgsConstructor;
import org.example.g4_localzero.dto.LoginRequest;
import org.example.g4_localzero.dto.RegisterRequest;
import org.example.g4_localzero.enums.UserRole;
import org.example.g4_localzero.model.User;
import org.example.g4_localzero.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .location(request.getLocation())
                .email(request.getEmail())
                .password(request.getPassword())
                .active(true)
                .roles(Set.of(UserRole.RESIDENT))
                .build();

        return userRepository.save(user);
    }

    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        if (!user.isActive()) {
            throw new RuntimeException(
                    "This account has been deactivated"
            );
        }

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return user;
    }
}