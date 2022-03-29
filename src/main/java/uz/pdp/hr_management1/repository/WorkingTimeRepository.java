package uz.pdp.hr_management1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.hr_management1.entity.WorkingTime;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface WorkingTimeRepository extends JpaRepository<WorkingTime, UUID> {

    @Query(value = "select * from working_time where process_time between ?1 and ?2 and turniket_id=?3", nativeQuery = true)
    List<WorkingTime> findAllByTurniketIdOrderByProcessTimeDescBetween(Date start,Date end,UUID turniket_id);
}
