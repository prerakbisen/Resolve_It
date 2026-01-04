package com.Info_intern.Hgs.repository;

import com.Info_intern.Hgs.model.Feedback;
import com.Info_intern.Hgs.model.Grievance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByGrievance(Grievance grievance);
}
