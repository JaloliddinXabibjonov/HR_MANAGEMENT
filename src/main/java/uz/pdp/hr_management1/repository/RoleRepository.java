package uz.pdp.hr_management1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hr_management1.entity.Role;
import uz.pdp.hr_management1.entity.enums.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(String manager);

    Optional<Role> findById(Integer id);
}
