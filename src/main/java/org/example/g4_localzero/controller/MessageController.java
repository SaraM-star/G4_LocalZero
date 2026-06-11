package org.example.g4_localzero.controller;

import lombok.RequiredArgsConstructor;
import org.example.g4_localzero.dto.MessageRequest;
import org.example.g4_localzero.model.Message;
import org.example.g4_localzero.service.MessageService;
import org.example.g4_localzero.service.SendMessageCommand;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

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

    @GetMapping("/inbox/{userId}")
    public List<Message> getInbox(@PathVariable Long userId) {
        return messageService.getInbox(userId);
    }

    @GetMapping("/sent/{userId}")
    public List<Message> getSentMessages(@PathVariable Long userId) {
        return messageService.getSentMessages(userId);
    }
}
