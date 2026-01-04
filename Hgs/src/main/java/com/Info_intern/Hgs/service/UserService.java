package com.Info_intern.Hgs.service;

import com.Info_intern.Hgs.model.User;
import com.Info_intern.Hgs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Random;
import java.util.Optional;
import com.Info_intern.Hgs.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired(required = false)
    private EmailService emailService;

    public User register(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash password

        return userRepository.save(user);
    }

    // Send OTP to email or (placeholder) phone for forgot-password
    public void sendOtp(String identifier) {
        User user = getUserByIdentifier(identifier);

        String otp = String.format("%06d", new Random().nextInt(1_000_000));
        long expiry = System.currentTimeMillis() + 10 * 60 * 1000; // 10 minutes

        user.setOtp(otp);
        user.setOtpExpiry(expiry);
        userRepository.save(user);

        // If email, send; if phone, just log (SMS provider not configured)
        if (identifier.contains("@")) {
            String subject = "Your password reset OTP";
            String text = "Use this OTP to reset your password: " + otp + " (valid for 10 minutes)";
            if (emailService != null) emailService.sendSimpleEmail(user.getEmail(), subject, text);
        } else {
            // TODO: integrate SMS provider. For now, print to console (developers can see logs).
            System.out.println("OTP for phone " + user.getPhone() + " is: " + otp);
        }
    }

    // Reset password using identifier and otp
    public void resetPasswordWithOtp(String identifier, String otp, String newPassword) {
        User user = getUserByIdentifier(identifier);

        if (user.getOtp() == null || user.getOtpExpiry() == null) {
            throw new RuntimeException("No OTP requested for this user");
        }

        if (!user.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (System.currentTimeMillis() > user.getOtpExpiry()) {
            throw new RuntimeException("OTP expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
    }

    private User getUserByIdentifier(String identifier) {
        Optional<User> maybe;
        if (identifier.contains("@")) {
            maybe = userRepository.findByEmail(identifier);
        } else {
            maybe = userRepository.findByPhone(identifier);
        }

        return maybe.orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid Email"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }

    // Return user (including password) for internal use
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Update basic profile fields (name, phone). Email and role are not changed here.
    public User updateProfile(String email, User update) {
        User existing = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (update.getName() != null && !update.getName().isBlank()) existing.setName(update.getName());
        if (update.getPhone() != null && !update.getPhone().isBlank()) existing.setPhone(update.getPhone());

        return userRepository.save(existing);
    }

    // Change password after verifying old password
    public void changePassword(String email, String oldPassword, String newPassword) {
        User existing = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(oldPassword, existing.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        existing.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(existing);
    }
}
