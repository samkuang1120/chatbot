package com.sk.chatbot.data;

public class Message {

    public Message(String role, String content) {

        this.role = role;
        this.content = content;

    }
    String role;
    String content;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}