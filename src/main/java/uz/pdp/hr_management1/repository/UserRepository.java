package uz.pdp.hr_management1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.hr_management1.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String username);

    Optional<User> findByEmailAndEmailCode(String email, String emailCode);

    @Query(value = "select * from users u inner join users_roles ur on u.id=ur.users_id and ur.roles_id=4",nativeQuery = true)
    List<User> findAllByRoleName();
}
