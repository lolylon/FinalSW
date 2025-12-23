package com.neckfurry.finalexam.controller;

import com.neckfurry.finalexam.dto.LoginRequest;
import com.neckfurry.finalexam.dto.LoginResponse;
import com.neckfurry.finalexam.dto.RegisterRequest;
import com.neckfurry.finalexam.entity.User;
import com.neckfurry.finalexam.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest registerRequest) {
        User user = authService.register(registerRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@RequestParam String token) {
        String newToken = authService.refreshToken(token);
        String username = authService.extractUsername(newToken);
        LoginResponse response = new LoginResponse(newToken, username);
        return ResponseEntity.ok(response);
    }
}
