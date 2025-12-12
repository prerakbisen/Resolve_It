package com.Info_intern.Hgs.controller;


import com.Info_intern.Hgs.model.User;
import com.Info_intern.Hgs.service.UserService;
import lombok.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.Info_intern.Hgs.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // frontend connected
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        User saved = userService.register(user);
        saved.setPassword(null);
        return saved;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest login) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));

        org.springframework.security.core.userdetails.UserDetails userDetails = userService.loadUserByUsername(login.getEmail());

        String token = jwtUtil.generateToken(userDetails);

        User user = userService.login(login.getEmail(), login.getPassword());
        user.setPassword(null);

        return new AuthResponse(token, user);
    }
}

@Data
class LoginRequest {
    private String email;
    private String password;
}
