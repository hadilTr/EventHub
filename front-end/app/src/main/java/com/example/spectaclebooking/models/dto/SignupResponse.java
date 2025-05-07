package com.example.spectaclebooking.models.dto;

import lombok.Data;

@Data
public class SignupResponse {
    private String email;

    // Constructeurs si vous n'utilisez pas Lombok
    public SignupResponse() {}

    public SignupResponse(String email) {
        this.email = email;
    }

    // Getters et setters si vous n'utilisez pas Lombok
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}