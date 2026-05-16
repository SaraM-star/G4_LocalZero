package org.example.g4_localzero.controller;

import jakarta.servlet.http.HttpSession;
import org.example.g4_localzero.model.Initiative;
import org.example.g4_localzero.model.User;
import org.example.g4_localzero.service.InitiativeService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/initiatives")
public class InitiativeController {

    private final InitiativeService initiativeService;

    public InitiativeController(InitiativeService initiativeService) {
        this.initiativeService = initiativeService;
    }

    @GetMapping("/current-user")
    public User getCurrentUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }



    @GetMapping
    public List<Initiative> getAllInitiatives(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            throw new RuntimeException("You must be logged in");
        }

        return initiativeService.getVisibleInitiatives(loggedInUser);
    }

    @PostMapping
    public Initiative createInitiative(@RequestBody Initiative initiative, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            throw new RuntimeException("You must be logged in");
        }

        return initiativeService.createInitiative(initiative, loggedInUser);
    }

    @PostMapping("/{id}/join")
    public Initiative joinInitiative(@PathVariable Long id, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            throw new RuntimeException("You must be logged in");
        }

        return initiativeService.joinInitiative(id, loggedInUser);
    }

    @PutMapping("/{id}")
    public Initiative updateInitiative(@PathVariable Long id,
                                       @RequestBody Initiative initiative,
                                       HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            throw new RuntimeException("You must be logged in");
        }

        return initiativeService.updateInitiative(id, initiative, loggedInUser);
    }

    @DeleteMapping("/{id}")
    public void deleteInitiative(@PathVariable Long id, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            throw new RuntimeException("You must be logged in");
        }

        initiativeService.deleteInitiative(id, loggedInUser);
    }
}