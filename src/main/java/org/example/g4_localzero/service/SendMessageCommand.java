package org.example.g4_localzero.service;

public class SendMessageCommand {

    private final MessageService messageService;
    private final Long senderId;
    private final Long receiverId;
    private final String subject;
    private final String content;

    public SendMessageCommand(MessageService messageService, Long senderId, Long receiverId, String subject, String content) {
        this.messageService = messageService;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.subject = subject;
        this.content = content;
    }

    public void execute() {
        messageService.sendMessage(senderId, receiverId, subject, content);
    }
}