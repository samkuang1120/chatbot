package com.sk.chatbot.repository.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String response;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public ChatMessage() {
    }

    public ChatMessage(Long id, String message, String response, LocalDateTime timestamp) {
        this.id = id;
        this.message = message;
        this.response = response;
        this.timestamp = timestamp;
    }

    public ChatMessage(String message, String responseText, LocalDateTime timestamp) {
        this.message = message;
        this.response = responseText;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
