package org.example.g4_localzero.repository;

import org.example.g4_localzero.model.Message;
import org.example.g4_localzero.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByReceiverOrderBySentAtDesc(User receiver);

    List<Message> findBySenderOrderBySentAtDesc(User sender);
}