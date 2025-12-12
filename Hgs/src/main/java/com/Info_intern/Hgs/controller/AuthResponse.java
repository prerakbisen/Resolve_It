package com.Info_intern.Hgs.controller;

import com.Info_intern.Hgs.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private User user;
}
