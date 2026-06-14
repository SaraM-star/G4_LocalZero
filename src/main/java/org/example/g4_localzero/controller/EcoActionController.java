package org.example.g4_localzero.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.g4_localzero.dto.EcoActionRequest;
import org.example.g4_localzero.model.User;
import org.example.g4_localzero.service.EcoActionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/eco-actions")
@RequiredArgsConstructor
public class EcoActionController {
    private final EcoActionService ecoActionService;

    @GetMapping
    public String ecoActionsPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("ecoActionRequest", new EcoActionRequest());
        model.addAttribute("ecoActions", ecoActionService.getActionsForUser(loggedInUser));
        model.addAttribute("totalCo2Saved", ecoActionService.getTotalCo2Saved(loggedInUser));

        return "eco-actions";
    }

    @PostMapping
    public String logEcoAction(@ModelAttribute EcoActionRequest request,
                               HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        ecoActionService.logAction(request, loggedInUser);

        return "redirect:/eco-actions";
    }
}
