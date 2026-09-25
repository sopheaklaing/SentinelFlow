package com.sopheak.SentinelFlow.dto;

public record AuthResponse(
        String accessToken,
        String tokenType
) {
}
