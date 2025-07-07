package com.traini.traini_backend.dto.auth;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("authorities")
    private List<String> authorities;

    public LoginResponse(String token, String refreshToken, List<String> authorities) {
        this.accessToken = token;
        this.refreshToken = refreshToken;
        this.authorities = authorities;
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

    @JsonProperty("authorities")
    public List<String> getAuthorities() { return authorities; }

    public void setAuthorities(List<String> authorities) { this.authorities = authorities; }
} 