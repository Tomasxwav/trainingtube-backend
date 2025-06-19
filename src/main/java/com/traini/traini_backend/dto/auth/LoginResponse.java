package com.traini.traini_backend.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    public LoginResponse(String token, String refreshToken) {
        this.accessToken = token;
        this.refreshToken = refreshToken;
    }

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }
    public void setToken(String token) {
        this.accessToken = token;
    }

    @JsonProperty("refresh_token")
    public String getRefreshToken() { return refreshToken; }
    
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
} 