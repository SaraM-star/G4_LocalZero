package org.example.g4_localzero.controller;

import lombok.RequiredArgsConstructor;
import org.example.g4_localzero.dto.MessageRequest;
import org.example.g4_localzero.model.Message;
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

    @GetMapping("/page/{userId}")
    public String messagesPage(@PathVariable Long userId, Model model) {
        model.addAttribute("inbox", messageService.getInbox(userId));
        model.addAttribute("sent", messageService.getSentMessages(userId));
        model.addAttribute("userId", userId);
        model.addAttribute("messageRequest", new MessageRequest());

        return "messages";
    }

    @PostMapping("/send-form")
    public String sendMessageForm(@ModelAttribute MessageRequest request) {

        SendMessageCommand command = new SendMessageCommand(
                messageService,
                request.getSenderId(),
                request.getReceiverId(),
                request.getSubject(),
                request.getContent()
        );

        command.execute();

        return "redirect:/messages/page/" + request.getSenderId();
    }

    @ResponseBody
    @PostMapping("/send")
    public String sendMessage(@RequestBody MessageRequest request) {

        SendMessageCommand command = new SendMessageCommand(
                messageService,
                request.getSenderId(),
                request.getReceiverId(),
                request.getSubject(),
                request.getContent()
        );

        command.execute();

        return "Message sent successfully";
    }

    @ResponseBody
    @GetMapping("/inbox/{userId}")
    public List<Message> getInbox(@PathVariable Long userId) {
        return messageService.getInbox(userId);
    }

    @ResponseBody
    @GetMapping("/sent/{userId}")
    public List<Message> getSentMessages(@PathVariable Long userId) {
        return messageService.getSentMessages(userId);
    }
}