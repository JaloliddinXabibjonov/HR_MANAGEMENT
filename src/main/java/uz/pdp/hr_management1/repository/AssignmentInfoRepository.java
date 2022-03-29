package uz.pdp.hr_management1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.hr_management1.entity.Assignment;
import uz.pdp.hr_management1.entity.AssignmentInfo;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface AssignmentInfoRepository extends JpaRepository<AssignmentInfo, UUID> {

//    @Query(value = "select * from assignment a inner join assignment_info ai  on a.created_at between ?3 and ?4 and a.id = ai.assignment_id and ai.user_id=?1 and task_info_status=?2 ", nativeQuery = true)
//    List<Assignment> findAllByUserIdAndTaskInfoStatusAndProcessDateBetween(UUID user_id, String taskInfoStatus, Timestamp processDate, Timestamp processDate2);

//    @Query(value = "select ai.assignment_id from assignment_info ai inner join assignment a  on  a.id = ai.assignment_id and ai.user_id=?1 and task_info_status=?2 and  a.created_at between ?3 and ?4  ", nativeQuery = true)
//    List<AssignmentInfo> findAllByUserIdAndTaskInfoStatusAndCreatedAtBetween(UUID user_id, String taskInfoStatus, Timestamp createdAt, Timestamp createdAt2);


}
