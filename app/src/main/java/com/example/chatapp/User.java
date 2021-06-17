package com.example.chatapp;

public class User {
    private String name;
    private String email;
    private String password;
    private String imageUrl;

    public User() {}

    public User(String name, String email, String password, String imageUrl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
