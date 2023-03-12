package com.sk.chatbot.service;

import com.sk.chatbot.data.ChatbotRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ChatbotService {
    public String getChatbotResponse(ChatbotRequest request);
}
