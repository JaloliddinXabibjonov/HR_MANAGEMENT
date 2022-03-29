package uz.pdp.hr_management1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.hr_management1.entity.*;
import uz.pdp.hr_management1.entity.enums.RoleName;
import uz.pdp.hr_management1.entity.enums.TaskInfoStatus;
import uz.pdp.hr_management1.payload.*;
import uz.pdp.hr_management1.payload.template.ApiResponse;
import uz.pdp.hr_management1.repository.*;
import uz.pdp.hr_management1.security.JwtProwider;
import uz.pdp.hr_management1.service.authority.Authority;

import java.sql.Timestamp;
import java.util.*;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProwider jwtProwider;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TurniketRepository turniketRepository;
    @Autowired
    WorkingTimeRepository workingTimeRepository;
    @Autowired
    AssignmentInfoRepository assignmentInfoRepository;
    @Autowired
    AssignmentRepository assignmentRepository;

    //MANAGERLAR ni ro`yxatdan o`tkazish uchun
    public ApiResponse registerManager(RegisterDto registerDto) {
        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail)
            return new ApiResponse("Bunday email bilan oldin ro`yxatdan o`tilgan", false);
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword("null");
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(registerDto.getRoleName())));
        user.setEmailCode(UUID.randomUUID().toString());

        //EMAILGA YUBORISH METODINI CHAQIRAMIZ
        Boolean email = sendEmail(user.getEmail(), user.getEmailCode());
        if (email) {
            userRepository.save(user);
            return new ApiResponse("Muvaffaqiyatli bajarildi", true);
        }
        return new ApiResponse("Xatolik yuz berdi, qayta urunib ko'ring", false);
    }

    public Boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("jaloliddin0292@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Akkountni tasdiqlash");

            mailMessage.setText("http://localhost:9090/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail);
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email, PasswordDto passwordDto) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordDto.getPassword().equals(passwordDto.getRePassword())) {
                user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
            } else {
                return new ApiResponse("Kiritilgan parol va takroriy parollar bir xil emas!", false);
            }
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Akkount muvaffaqiyatli tasdiqlandi va parol o`rnatildi", true);
        }
        return new ApiResponse("Akkount allaqachon tasdiqlangan", false);
    }


    // EMPLOYEE ni ro`yxatdan o`tkazish uchun
    public ApiResponse registerEmployee(RegisterDto registerDto) {
        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail)
            return new ApiResponse("Bunday email bilan oldin ro`yxatdan o`tilgan", false);
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPassword("null");
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.EMPLOYEE.name())));
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);

        //EMAILGA YUBORISH METODINI CHAQIRAMIZ
        sendEmail(user.getEmail(), user.getEmailCode());
        return new ApiResponse("Muvaffaqiyatli ro`yxatdan o`tdingiz, akkountingizni aktivlashtirish uchun emailingizni tasdiqlang", true);
    }

    public boolean workingTime(TurniketDto turniketDto) {
        Optional<Turniket> optionalTurniket = turniketRepository.findById(turniketDto.getTurniketId());
        if (optionalTurniket.isPresent()) {
            WorkingTime workingTime = new WorkingTime();
            workingTime.setTurniket(optionalTurniket.get());
            workingTime.setProcessType(turniketDto.isProcessType());
            workingTime.setProcessTime(new Date());
            workingTimeRepository.save(workingTime);
            return true;
        }
        return false;
    }


    public List<User> getAllEmployees() {
        return userRepository.findAllByRoleName();
    }


    public WorkerInfo workerInfo(WorkerInfoDto workerInfoDto) {
        Optional<Turniket> optionalTurniket = turniketRepository.findTurniketByUserId(workerInfoDto.getId());
        if (optionalTurniket.isPresent()) {
            WorkerInfo workerInfo=new WorkerInfo();
            Turniket turniket = optionalTurniket.get();
            List<WorkingTime> workingTimeList = workingTimeRepository.findAllByTurniketIdOrderByProcessTimeDescBetween(new Date(workerInfoDto.getStart()), new Date(workerInfoDto.getEnd()), turniket.getId());
            List<Assignment> assignmentInfoList = assignmentRepository.findAllByEmployeeIdAndStatusAndCreatedAtBetween(workerInfoDto.getId(), TaskInfoStatus.DONE.name(), new Timestamp(workerInfoDto.getStart()), new Timestamp(workerInfoDto.getEnd()));
            workerInfo.setWorkingTimes(workingTimeList);
            workerInfo.setAssignmentInfos(assignmentInfoList);
            return workerInfo;
        }
        return new WorkerInfo();
    }


}
