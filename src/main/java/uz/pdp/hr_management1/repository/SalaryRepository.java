package uz.pdp.hr_management1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hr_management1.entity.Salary;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface SalaryRepository extends JpaRepository<Salary, UUID> {
    List<Salary> findAllByProcessTimeBetween(Date processTime, Date processTime2);

    List<Salary> findAllByOwnerId(UUID owner_id);

}
