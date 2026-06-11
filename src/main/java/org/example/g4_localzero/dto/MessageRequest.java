package org.example.g4_localzero.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageRequest {

    private Long senderId;
    private Long receiverId;
    private String subject;
    private String content;
}