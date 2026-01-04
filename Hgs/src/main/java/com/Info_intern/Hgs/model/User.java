package com.Info_intern.Hgs.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;  // ✅ Add this field

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;  // admin/patient/staff

    // OTP for forgot-password flow (nullable)
    @Column
    private String otp;

    // expiry time in epoch millis for OTP
    @Column
    private Long otpExpiry;
}
