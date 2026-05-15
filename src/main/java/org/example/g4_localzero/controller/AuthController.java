package org.example.g4_localzero.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.g4_localzero.dto.LoginRequest;
import org.example.g4_localzero.dto.RegisterRequest;
import org.example.g4_localzero.model.User;
import org.example.g4_localzero.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest request, Model model) {
        try {
            authService.register(request);
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest request, HttpSession session, Model model) {
        try {
            User user = authService.login(request);
            session.setAttribute("loggedInUser", user);
            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}