package com.Info_intern.Hgs.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grievances")
public class Grievance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String category;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "feedback_given", nullable = false)
    private Boolean feedbackGiven = Boolean.FALSE;


    @Column(nullable = false)
    private String status = "PENDING"; // PENDING or COMPLETED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // who submitted

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "grievance", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<GrievanceImage> images = new ArrayList<>();

}
