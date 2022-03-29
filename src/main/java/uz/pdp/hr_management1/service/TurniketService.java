package uz.pdp.hr_management1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.hr_management1.entity.Turniket;
import uz.pdp.hr_management1.entity.User;
import uz.pdp.hr_management1.payload.template.ApiResponse;
import uz.pdp.hr_management1.repository.TurniketRepository;
import uz.pdp.hr_management1.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class TurniketService {
    @Autowired
    TurniketRepository turniketRepository;
    @Autowired
    UserRepository userRepository;

    public ApiResponse addTurniket(UUID userId) {
        boolean exists = turniketRepository.existsByUserId(userId);
        if (exists)
            return new ApiResponse("Ushbu xodimga avval turniket berilgan", false);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Turniket turniket = new Turniket();
            turniket.setUser(optionalUser.get());
            turniketRepository.save(turniket);
            return new ApiResponse("Muvaffaqiyatli saqlandi", true);
        }
        return new ApiResponse("Bunday xodim topilmadi", false);
    }
}
