package com.Info_intern.Hgs.controller;


import com.Info_intern.Hgs.model.User;
import com.Info_intern.Hgs.service.UserService;
import lombok.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.Info_intern.Hgs.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

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

    @PostMapping("/forgot/request")
    public ResponseEntity<?> forgotRequest(@RequestBody IdentifierRequest req) {
        try {
            userService.sendOtp(req.getIdentifier());
            return ResponseEntity.ok().body("OTP sent if the account exists");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/forgot/reset")
    public ResponseEntity<?> forgotReset(@RequestBody ResetRequest req) {
        try {
            userService.resetPasswordWithOtp(req.getIdentifier(), req.getOtp(), req.getNewPassword());
            return ResponseEntity.ok().body("Password updated successfully");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}

@Data
class LoginRequest {
    private String email;
    private String password;
}

class IdentifierRequest {
    private String identifier;

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
}

class ResetRequest {
    private String identifier;
    private String otp;
    private String newPassword;

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
