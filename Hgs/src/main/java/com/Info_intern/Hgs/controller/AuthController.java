package com.Info_intern.Hgs.controller;


import com.Info_intern.Hgs.model.User;
import com.Info_intern.Hgs.service.UserService;
import lombok.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // frontend connected
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody LoginRequest login) {
        return userService.login(login.getEmail(), login.getPassword());
    }
}

@Data
class LoginRequest {
    private String email;
    private String password;
}
