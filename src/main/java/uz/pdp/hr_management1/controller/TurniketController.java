package uz.pdp.hr_management1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hr_management1.payload.template.ApiResponse;
import uz.pdp.hr_management1.repository.TurniketRepository;
import uz.pdp.hr_management1.service.TurniketService;

import java.util.UUID;

@RestController
@RequestMapping("/api/turniket")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TurniketController {
    @Autowired
    TurniketService turniketService;

    /** TURNIKET QO`SHISH */
    @PreAuthorize(value = "hasAuthority('HR_MANAGER')")
    @PostMapping("/add/{userId}")
    public HttpEntity<?> addTurniket(@PathVariable UUID userId){
        ApiResponse apiResponse=turniketService.addTurniket(userId);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }
}
