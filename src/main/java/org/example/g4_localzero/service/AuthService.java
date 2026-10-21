package org.example.g4_localzero.service;

import lombok.RequiredArgsConstructor;
import org.example.g4_localzero.dto.LoginRequest;
import org.example.g4_localzero.dto.RegisterRequest;
import org.example.g4_localzero.enums.UserRole;
import org.example.g4_localzero.model.User;
import org.example.g4_localzero.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .location(request.getLocation())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(UserRole.RESIDENT))
                .build();

        return userRepository.save(user);
    }

    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Wrong password");
        }

        return user;
    }

    public void updateUserRole(Long userId, String roleName){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getRoles().clear();

        UserRole newRole = UserRole.valueOf(roleName);
        user.getRoles().add(newRole);
        userRepository.save(user);

    }

    public User getUserbyId(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

    }
}