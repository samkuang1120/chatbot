package com.sk.chatbot.data;

import java.util.List;

public class OpenAIReqMessages {

    private String model;

    public OpenAIReqMessages(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    private List<Message> messages;

}
