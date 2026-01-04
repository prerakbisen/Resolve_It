package com.Info_intern.Hgs.controller;

import com.Info_intern.Hgs.model.User;
import com.Info_intern.Hgs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public User getProfile(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getByEmail(email);
        user.setPassword(null);
        return user;
    }

    @PutMapping
    public User updateProfile(Authentication authentication, @RequestBody User update) {
        String email = authentication.getName();
        User saved = userService.updateProfile(email, update);
        saved.setPassword(null);
        return saved;
    }

    @PostMapping("/change-password")
    public String changePassword(Authentication authentication, @RequestBody ChangePasswordRequest req) {
        String email = authentication.getName();
        userService.changePassword(email, req.getOldPassword(), req.getNewPassword());
        return "Password changed";
    }

}

class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;

    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
