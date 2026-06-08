package org.example.g4_localzero.controller;

import jakarta.servlet.http.HttpSession;
import org.example.g4_localzero.model.Forum;
import org.example.g4_localzero.model.Initiative;
import org.example.g4_localzero.model.User;
import org.example.g4_localzero.service.ForumService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forums")
public class ForumController {

    private final ForumService forumService;

    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }

    @GetMapping
    public List<Forum> getVisibleForums(HttpSession session) {
        User loggedInUser = getLoggedInUser(session);
        return forumService.getVisibleForums(loggedInUser);
    }

    @GetMapping("/{forumId}/initiatives")
    public List<Initiative> getInitiativesForForum(@PathVariable Long forumId,
                                                   HttpSession session) {
        User loggedInUser = getLoggedInUser(session);
        return forumService.getInitiativesForForum(forumId, loggedInUser);
    }

    private User getLoggedInUser(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            throw new RuntimeException("You must be logged in");
        }

        return loggedInUser;
    }
}