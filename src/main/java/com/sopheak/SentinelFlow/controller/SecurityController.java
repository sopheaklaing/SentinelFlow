package com.sopheak.SentinelFlow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/security")
public class SecurityController {

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> securityStatus() {

        return ResponseEntity.ok(
                Map.of(
                        "service", "sentinelflow",
                        "security", "ACTIVE",
                        "status", "UP"
                )
        );
    }
}