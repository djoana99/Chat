package com.example.chatapp;

public class ChatUser {
    private String id;
    private String image;
    private String name;

    public ChatUser(String id, String image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
