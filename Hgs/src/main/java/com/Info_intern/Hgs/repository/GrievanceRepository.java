package com.Info_intern.Hgs.repository;

import com.Info_intern.Hgs.model.Grievance;
import com.Info_intern.Hgs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GrievanceRepository extends JpaRepository<Grievance, Long> {
    List<Grievance> findByUser(User user);
}
