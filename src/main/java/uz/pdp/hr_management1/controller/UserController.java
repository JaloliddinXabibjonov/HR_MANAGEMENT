package uz.pdp.hr_management1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hr_management1.entity.Role;
import uz.pdp.hr_management1.entity.User;
import uz.pdp.hr_management1.payload.*;
import uz.pdp.hr_management1.payload.template.ApiResponse;
import uz.pdp.hr_management1.repository.UserRepository;
import uz.pdp.hr_management1.security.JwtProwider;
import uz.pdp.hr_management1.service.AuthService;
import uz.pdp.hr_management1.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AuthService authService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProwider jwtProwider;
    @Autowired
    UserRepository userRepository;

    /** MANAGERLARNI RO'YXATDAN O'TKAZISH */
    @PreAuthorize(value = "hasAuthority('DIRECTOR')")
    @PostMapping("/register/manager")
    public HttpEntity<?> registerManager(@RequestBody RegisterDto registerDto){
        ApiResponse apiResponse = authService.registerManager(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto){
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(),
                    loginDto.getPassword()
            ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Optional<User> optionalUser = userRepository.findByEmail(loginDto.getUsername());
            return jwtProwider.generateToken(loginDto.getUsername(),  optionalUser.get().getRoles());
        }catch (Exception e){
        return "Login yoki parol xato";
        }
    }

    /**POCHTANI TASDIQLASH VA YANGI PAROL O'RNATISH*/
    @PostMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam String emailCode, @RequestParam String email, @RequestBody PasswordDto passwordDto){
        ApiResponse apiResponse=authService.verifyEmail(emailCode, email, passwordDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    /** ISHCHILARNI RO'YXATDAN O'TKAZISH */
    @PreAuthorize(value = "hasAuthority('HR_MANAGER')")
    @PostMapping("/register/employee")
    public HttpEntity<?> registerEmployee(@RequestBody RegisterDto registerDto){
        ApiResponse apiResponse = authService.registerEmployee(registerDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    /** ISHCHILARNI KIRISH CHIQISH VAQTLARINI ANIQLASH UCHUN TURNIKETNI ISHLATISH METODI */
    @PostMapping("/turniket")
    public boolean turniket(@RequestBody TurniketDto turniketDto){
        return authService.workingTime(turniketDto);
    }

    /** DIREKTOR VA HR_MAGAGERGA XODIMLAR RO`YXATINI OLIB KELISH UCHUN */
    @PreAuthorize(value = "hasAnyAuthority('DIRECTOR', 'HR_MANAGER')")
    @GetMapping("/getAllEmployees")
    public List<User> getAllEmployees(){
        return authService.getAllEmployees();
    }

    /** XODIMNING BERILGAN VAQT ORALIG'I BO`YICHA ISHGA kelib-ketishi va bajargan tasklari haqida ma’lumot  */
    @PreAuthorize(value = "hasAnyAuthority('DIRECTOR', 'HR_MANAGER', 'MANAGER')")
    @GetMapping("/workerInfo")
    public WorkerInfo workerInfo(@RequestBody WorkerInfoDto workerInfoDto){
        return authService.workerInfo(workerInfoDto);
    }
}

