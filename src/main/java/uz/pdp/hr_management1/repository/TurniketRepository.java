package uz.pdp.hr_management1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.hr_management1.entity.Turniket;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TransferQueue;

public interface TurniketRepository extends JpaRepository<Turniket, UUID> {
    boolean existsByUserId(UUID user_id);
    Optional<Turniket> findTurniketByUserId(UUID user_id);

}
