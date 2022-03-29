package uz.pdp.hr_management1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hr_management1.entity.Assignment;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface AssignmentRepository extends JpaRepository<Assignment, UUID> {
    List<Assignment> findAllByEmployeeIdAndStatusOrderByCreatedAtDesc(UUID employee_id, String status);
    List<Assignment> findAllByOwnerAssignmentIdOrderByCreatedAtDesc(UUID owner_id);

    List<Assignment> findAllByEmployeeIdAndStatusAndCreatedAtBetween(UUID employee_id, String status, Timestamp createdAt, Timestamp createdAt2);

}
