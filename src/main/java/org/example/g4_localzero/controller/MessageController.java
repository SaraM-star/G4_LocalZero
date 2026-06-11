package org.example.g4_localzero.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.g4_localzero.dto.MessageRequest;
import org.example.g4_localzero.model.Message;
import org.example.g4_localzero.model.User;
import org.example.g4_localzero.service.MessageService;
import org.example.g4_localzero.service.SendMessageCommand;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public String messagesPage(HttpSession session, Model model) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Long userId = loggedInUser.getId();

        model.addAttribute("inbox", messageService.getInbox(userId));
        model.addAttribute("sent", messageService.getSentMessages(userId));
        model.addAttribute("userId", userId);
        model.addAttribute("users", messageService.getAllUsers());
        model.addAttribute("messageRequest", new MessageRequest());

        return "messages";
    }

    @PostMapping("/send-form")
    public String sendMessageForm(@ModelAttribute MessageRequest request,
                                  HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        SendMessageCommand command = new SendMessageCommand(
                messageService,
                loggedInUser.getId(),
                request.getReceiverId(),
                request.getSubject(),
                request.getContent()
        );

        command.execute();

        return "redirect:/messages";
    }

    @ResponseBody
    @PostMapping("/send")
    public String sendMessage(@RequestBody MessageRequest request,
                              HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "User is not logged in";
        }

        SendMessageCommand command = new SendMessageCommand(
                messageService,
                loggedInUser.getId(),
                request.getReceiverId(),
                request.getSubject(),
                request.getContent()
        );

        command.execute();

        return "Message sent successfully";
    }

    @ResponseBody
    @GetMapping("/inbox")
    public List<Message> getInbox(HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            throw new RuntimeException("User is not logged in");
        }

        return messageService.getInbox(loggedInUser.getId());
    }

    @ResponseBody
    @GetMapping("/sent")
    public List<Message> getSentMessages(HttpSession session) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            throw new RuntimeException("User is not logged in");
        }

        return messageService.getSentMessages(loggedInUser.getId());
    }
}