package uz.pdp.hr_management1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hr_management1.entity.Salary;
import uz.pdp.hr_management1.payload.GiveSalaryDto;
import uz.pdp.hr_management1.payload.template.ApiResponse;
import uz.pdp.hr_management1.service.SalaryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/salary")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SalaryController {

    @Autowired
    SalaryService salaryService;

    /**OYLIK BERISH*/
    @PreAuthorize(value = "hasAnyAuthority('DIRECTOR', 'HR_MANAGER')")
    @PostMapping("/add")
    public HttpEntity<?> giveSalary(@RequestBody GiveSalaryDto salaryDto) {
        ApiResponse apiResponse = salaryService.giveSalary(salaryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }

    /**BERILGAN VAQT ORALIG'I BO`YICHA XODIMGA BERILGAN OYLIKLAR HAQIDA MA'LUMOT*/
    @PreAuthorize(value = "hasAnyAuthority('DIRECTOR', 'HR_MANAGER')")
    @GetMapping("/salaryInfoByDateBetween/{startDate}/{endDate}")
    public List<Salary> salaryInfoByDateBetween(@PathVariable long startDate, @PathVariable long endDate){
        return salaryService.salaryInfoByDateBetween(startDate,endDate);
    }

    /**TANLANGAN XODIM BO`YICHA XODIMGA BERILGAN OYLIKLAR HAQIDA MA'LUMOT*/
    @PreAuthorize(value = "hasAnyAuthority('DIRECTOR', 'HR_MANAGER')")
    @GetMapping("/salaryInfoByUserId/{id}")
    public List<Salary> salaryInfoByUserId(@PathVariable UUID id){
        return salaryService.salaryInfoByUserId(id);
    }


}
