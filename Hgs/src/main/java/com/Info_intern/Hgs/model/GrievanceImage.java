package com.Info_intern.Hgs.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grievance_images")
public class GrievanceImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grievance_id")
    private Grievance grievance;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String imageData;
}
