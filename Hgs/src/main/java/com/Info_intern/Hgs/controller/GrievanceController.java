package com.Info_intern.Hgs.controller;

import com.Info_intern.Hgs.model.Feedback;
import com.Info_intern.Hgs.model.Grievance;
import com.Info_intern.Hgs.service.GrievanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/grievances")
@CrossOrigin(origins = "*")
public class GrievanceController {

    @Autowired
    private GrievanceService grievanceService;

    private final Logger logger = LoggerFactory.getLogger(GrievanceController.class);

    @PostMapping
    @PreAuthorize("hasAuthority('patient')")
    public org.springframework.http.ResponseEntity<?> submit(@RequestBody Grievance grievance, Principal principal) {
        if (principal == null) {
            return org.springframework.http.ResponseEntity.status(401).body("Unauthorized");
        }
        try {
            Grievance saved = grievanceService.submitGrievance(grievance, principal.getName());
            return org.springframework.http.ResponseEntity.ok(saved);
        } catch (RuntimeException ex) {
            return org.springframework.http.ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return org.springframework.http.ResponseEntity.status(500).body("Server error: " + ex.getMessage());
        }
    }

    @GetMapping("/mine")
    @PreAuthorize("hasAuthority('patient')")
    public List<Grievance> mine(Principal principal) {
        return grievanceService.getByUser(principal.getName());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('admin') or hasAuthority('staff')")
    public List<Grievance> all() {
        return grievanceService.getAll();
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('staff') or hasAuthority('admin')")
    public org.springframework.http.ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam String status, Principal principal) {
        logger.info("Received status update request: id={}, status={}, principal={}.", id, status, principal==null?null:principal.getName());
        try {
            Grievance updated = grievanceService.updateStatus(id, status);
            return org.springframework.http.ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            logger.warn("Failed to update grievance status: {}", ex.getMessage());
            return org.springframework.http.ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Error updating grievance status", ex);
            return org.springframework.http.ResponseEntity.status(500).body("Server error");
        }
    }

    @PostMapping("/{id}/feedback")
    @PreAuthorize("isAuthenticated()")
    public Feedback feedback(@PathVariable Long id, @RequestBody String comment, Principal principal) {
        return grievanceService.addFeedback(id, principal.getName(), comment);
    }
}
