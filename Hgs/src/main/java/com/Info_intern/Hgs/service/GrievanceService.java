package com.Info_intern.Hgs.service;

import com.Info_intern.Hgs.model.Feedback;
import com.Info_intern.Hgs.model.Grievance;
import com.Info_intern.Hgs.model.User;
import com.Info_intern.Hgs.repository.FeedbackRepository;
import com.Info_intern.Hgs.repository.GrievanceRepository;
import com.Info_intern.Hgs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrievanceService {

    @Autowired
    private GrievanceRepository grievanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private EmailService emailService;

    public Grievance submitGrievance(Grievance g, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        if (g.getTitle() == null || g.getTitle().trim().isEmpty()) throw new RuntimeException("Title is required");
        g.setUser(user);
        if (g.getStatus() == null) g.setStatus("PENDING");
        g.setCreatedAt(java.time.LocalDateTime.now());
        Grievance saved = grievanceRepository.save(g);
        return saved;
    }

    public List<Grievance> getAll() {
        return grievanceRepository.findAll();
    }

    public List<Grievance> getByUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return grievanceRepository.findByUser(user);
    }

    public Grievance updateStatus(Long id, String status) {
        Grievance g = grievanceRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        g.setStatus(status);
        Grievance saved = grievanceRepository.save(g);

        if ("COMPLETED".equalsIgnoreCase(status) || "RESOLVED".equalsIgnoreCase(status)) {
            // notify user and admins
            String subject = "Grievance Resolved: " + g.getTitle();
            String text = "Your grievance with id " + g.getId() + " has been marked as completed.";
            if (g.getUser() != null && g.getUser().getEmail() != null)
                emailService.sendSimpleEmail(g.getUser().getEmail(), subject, text);

            userRepository.findAll().stream()
                    .filter(u -> "admin".equalsIgnoreCase(u.getRole()))
                    .forEach(admin -> emailService.sendSimpleEmail(admin.getEmail(), "Grievance completed: " + g.getId(), "Grievance completed by staff."));
        }

        return saved;
    }

    public Feedback addFeedback(Long grievanceId, String userEmail, String comment) {
        Grievance g = grievanceRepository.findById(grievanceId).orElseThrow(() -> new RuntimeException("Grievance not found"));
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

        Feedback f = new Feedback();
        f.setGrievance(g);
        f.setUser(user);
        f.setComment(comment);
        Feedback saved = feedbackRepository.save(f);

        // attach feedback summary to grievance for quick access
        g.setFeedback(comment);
        g.setFeedbackGiven(true);
        grievanceRepository.save(g);

        // notify admins and staff
        String subject = "New feedback for grievance: " + g.getId();
        String text = "Feedback: " + comment;
        userRepository.findAll().stream()
                .filter(u -> "admin".equalsIgnoreCase(u.getRole()) || "staff".equalsIgnoreCase(u.getRole()))
                .forEach(u -> emailService.sendSimpleEmail(u.getEmail(), subject, text));

        return saved;
    }
}
