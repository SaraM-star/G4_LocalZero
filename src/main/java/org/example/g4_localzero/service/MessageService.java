package org.example.g4_localzero.service;

import lombok.RequiredArgsConstructor;
import org.example.g4_localzero.model.Message;
import org.example.g4_localzero.model.User;
import org.example.g4_localzero.repository.MessageRepository;
import org.example.g4_localzero.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public void sendMessage(Long senderId, Long receiverId, String subject, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .subject(subject)
                .content(content)
                .sentAt(LocalDateTime.now())
                .readMessage(false)
                .build();

        messageRepository.save(message);
    }

    public List<Message> getInbox(Long userId) {
        User receiver = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return messageRepository.findByReceiverOrderBySentAtDesc(receiver);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Message> getSentMessages(Long userId) {
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return messageRepository.findBySenderOrderBySentAtDesc(sender);
    }
}