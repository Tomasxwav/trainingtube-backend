package com.traini.traini_backend.dto.auth;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenRequest {
    @JsonProperty("refresh_token") // Anotación para mapear el campo JSON
    private String refreshToken;

    public RefreshTokenRequest() {}

    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}