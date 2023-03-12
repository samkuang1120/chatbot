package com.sk.chatbot.repository;
import com.sk.chatbot.repository.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository  extends JpaRepository<ChatMessage, Long> {
    ChatMessage findById(long id);
    List<ChatMessage> findAllByOrderByTimestampAsc();

    List<ChatMessage> findAllByOrderByTimestampDesc();
}
