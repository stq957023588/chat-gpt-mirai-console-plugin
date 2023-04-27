package com.fool.mirai.console.openai;

public class Message {
    /**
     * one of system,user,assistant
     */
    private String role;

    private String content;

    private String name;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
