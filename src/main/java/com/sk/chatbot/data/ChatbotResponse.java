package com.sk.chatbot.data;

import java.time.LocalDateTime;

public class ChatbotResponse {
    private String message;
    private String response;
    private LocalDateTime timestamp;

    public ChatbotResponse(String message, String responseText, LocalDateTime timestamp) {
        this.message = message;
        this.response = responseText;
        this.timestamp = timestamp;
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

    @Override
    public String toString() {
        return "ChatbotResponse{" +
                "message='" + message + '\'' +
                ", response='" + response + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
