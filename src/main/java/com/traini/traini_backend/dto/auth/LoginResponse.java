package com.traini.traini_backend.dto.auth;

public class LoginResponse {
    private String access_token;

    public LoginResponse(String token) {
        this.access_token = token;
    }

    
    public String getAccessToken() {
        return access_token;
    }
    public void setToken(String token) {
        this.access_token = token;
    }
} 