package com.traini.traini_backend.dto.auth;

public class LoginResponse {
    private String access_token;
    private String refresh_token;

    public LoginResponse(String token, String refreshToken) {
        this.access_token = token;
        this.refresh_token = refreshToken;
    }

    
    public String getAccessToken() {
        return access_token;
    }
    public void setToken(String token) {
        this.access_token = token;
    }

    public String getRefreshToken() { return refresh_token; }
    public void setRefreshToken(String refreshToken) { this.refresh_token = refreshToken; }
} 