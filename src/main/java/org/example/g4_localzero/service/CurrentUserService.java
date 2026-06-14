package org.example.g4_localzero.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.g4_localzero.exception.UnauthenticatedException;
import org.example.g4_localzero.model.User;
import org.example.g4_localzero.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public User requireActiveUser(HttpSession session) {
        User sessionUser =
                (User) session.getAttribute("loggedInUser");

        if (sessionUser == null || sessionUser.getId() == null) {
            throw new UnauthenticatedException(
                    "You must be logged in"
            );
        }

        User currentUser = userRepository.findById(sessionUser.getId())
                .orElseThrow(() ->
                        new UnauthenticatedException(
                                "User account no longer exists"
                        )
                );

        if (!currentUser.isActive()) {
            session.invalidate();

            throw new UnauthenticatedException(
                    "This account has been deactivated"
            );
        }

        return currentUser;
    }

    public User findActiveUserOrNull(HttpSession session) {
        try {
            return requireActiveUser(session);
        } catch (UnauthenticatedException exception) {
            return null;
        }
    }
}