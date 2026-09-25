package com.sopheak.SentinelFlow.dto;

public record RegisterRequest(
    String name ,
    String email ,
    String password
) {
}
