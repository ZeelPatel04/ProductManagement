package com.ProductManagement.controller;

import com.ProductManagement.config.SecurityConfig;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        if ("admin".equals(request.getUsername()) && "password".equals(request.getPassword())) {
            String token = SecurityConfig.generateToken(request.getUsername(), "ADMIN");
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } else if ("user".equals(request.getUsername()) && "password".equals(request.getPassword())) {
            String token = SecurityConfig.generateToken(request.getUsername(), "USER");
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @Data
    static class AuthRequest {
        private String username;
        private String password;
    }
}
