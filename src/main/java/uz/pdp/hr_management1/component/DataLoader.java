package uz.pdp.hr_management1.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.hr_management1.entity.Role;
import uz.pdp.hr_management1.entity.User;
import uz.pdp.hr_management1.entity.enums.RoleName;
import uz.pdp.hr_management1.repository.RoleRepository;
import uz.pdp.hr_management1.repository.UserRepository;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
@Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.sql.init.mode}")
    private String mode;


    @Override
    public void run(String... args) {
        if (mode.equals("always")) {
            roleRepository.save(new Role(1, RoleName.DIRECTOR.toString()));
            roleRepository.save(new Role(2, RoleName.MANAGER.toString()));
            roleRepository.save(new Role(3, RoleName.HR_MANAGER.toString()));
            roleRepository.save(new Role(4, RoleName.EMPLOYEE.toString()));
            userRepository.save(new User("Xabibjonov@mail.ru", passwordEncoder.encode("Direktor0101"), Collections.singleton(roleRepository.findById(1).get()), true));
        }
    }
}
