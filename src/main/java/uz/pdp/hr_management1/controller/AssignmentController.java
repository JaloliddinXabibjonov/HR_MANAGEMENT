package uz.pdp.hr_management1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.hr_management1.entity.Assignment;
import uz.pdp.hr_management1.payload.AssignmentDto;
import uz.pdp.hr_management1.payload.AssignmentInfoDto;
import uz.pdp.hr_management1.payload.template.ApiResponse;
import uz.pdp.hr_management1.service.AssignmentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/assignment")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AssignmentController {
    @Autowired
    AssignmentService assignmentService;

    /**DIRECTOR TOMONIDAN MANAGERLARGA VAZIFA BERISH*/
    @PreAuthorize(value = "hasAuthority('DIRECTOR')")
    @PostMapping("/add")
    public HttpEntity<?> addAssignmentToManagers(@RequestBody AssignmentDto assignmentDto){
        ApiResponse apiResponse=assignmentService.addAssignmentToManagers(assignmentDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    /**MANAGERLAR TOMONIDAN MANAGERLARGA VAZIFA BERISH*/
    @PreAuthorize(value = "hasAnyAuthority('MANAGER', 'HR_MANAGER')")
    @PostMapping("/addToEmployee")
    public HttpEntity<?> addAssignmentToEmployees(@RequestBody AssignmentDto assignmentDto){
        ApiResponse apiResponse=assignmentService.addAssignmentToEmployees(assignmentDto);
        return ResponseEntity.status(apiResponse.isSuccess()?201:409).body(apiResponse);
    }

    /** VAZIFA QABUL QILISH */
    @PostMapping("/accept/{id}")
    public HttpEntity<?> acceptAssignment(@PathVariable UUID id){
        ApiResponse apiResponse=assignmentService.acceptAssignment(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    /**VAZIFANI YAKUNLASH*/
    @PostMapping("/completeTheTask/{id}")
    public HttpEntity<?> completeTheTask(@PathVariable UUID id){
        ApiResponse apiResponse=assignmentService.completeTheTask(id);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    /**USERNI BAJARISH JARAYONIDAGI VAZIFALARINI OLIB KELISH UCHUN*/
    @GetMapping("/getMyAssignments")
    public List<Assignment> getMyAssignments(){
        return assignmentService.getMyAssignments();
    }

    /**USERGA UNGA BIRIKTIRLGAN YANGI VAZIFALARNI OLIB KELISH UCHUN*/
    @GetMapping("/getMyNewAssignments")
    public List<Assignment> getMyNewAssignments(){
        return assignmentService.getMyNewAssignments();
    }



    /**DIREKTOR VA MANAGERLARGA ULAR BIRIKTIRGAN VAZIFALARNI OLIB KELISH UCHUN*/
    @GetMapping("/getTasksIGave")
    public List<Assignment> getTasksIGave(){
        return assignmentService.getTasksIGave();
    }

    /** userni o’z vaqtida bajargan yoki o’z vaqtida bajara olmayotgan tasklar*/
    @PreAuthorize(value = "hasAnyAuthority('DIRECTOR', 'HR_MANAGER')")
    @GetMapping("/getAssignmentInfoByUserId/{id}")
    public AssignmentInfoDto getAssignmentInfoByUserId(@PathVariable UUID id){
        return assignmentService.getAssignmentInfoByUserId(id);
    }

}
