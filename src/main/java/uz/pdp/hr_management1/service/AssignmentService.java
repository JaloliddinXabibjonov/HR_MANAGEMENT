package uz.pdp.hr_management1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.pdp.hr_management1.entity.Assignment;
import uz.pdp.hr_management1.entity.AssignmentInfo;
import uz.pdp.hr_management1.entity.Role;
import uz.pdp.hr_management1.entity.User;
import uz.pdp.hr_management1.entity.enums.RoleName;
import uz.pdp.hr_management1.entity.enums.TaskInfoStatus;
import uz.pdp.hr_management1.entity.enums.TaskStatus;
import uz.pdp.hr_management1.payload.AssignmentDto;
import uz.pdp.hr_management1.payload.AssignmentInfoDto;
import uz.pdp.hr_management1.payload.template.ApiResponse;
import uz.pdp.hr_management1.repository.AssignmentInfoRepository;
import uz.pdp.hr_management1.repository.AssignmentRepository;
import uz.pdp.hr_management1.repository.UserRepository;
import uz.pdp.hr_management1.service.authority.Authority;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AssignmentService {
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AssignmentRepository assignmentRepository;
    @Autowired
    AssignmentInfoRepository assignmentInfoRepository;

    public ApiResponse addAssignmentToManagers(AssignmentDto assignmentDto) {
        Optional<User> optionalUser = userRepository.findById(assignmentDto.getEmployeeId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();  // bu user topshiriq yuborilayotgan user
            String roleName = "";
            for (Role role : user.getRoles()) {
                roleName = role.getRoleName();
            }
            if (roleName.equals(RoleName.EMPLOYEE.name()))
                return new ApiResponse("Siz xodimlarga vazifa bera olmaysiz", false);
            Assignment assignment = new Assignment();
            assignment.setName(assignmentDto.getName());
            assignment.setComment(assignmentDto.getComment());
            assignment.setOwnerAssignment(new Authority().getUser());
            assignment.setStatus(TaskStatus.NEW.name());
            assignment.setEmployee(user);
            try {
                String finishTime = assignmentDto.getFinishTime();
                Date finishDate = new SimpleDateFormat("dd/MM/yyyy").parse(finishTime);
                assignment.setFinishTime(finishDate);
                Assignment savedAssignment = assignmentRepository.save(assignment);
                AssignmentInfo assignmentInfo = new AssignmentInfo();
                assignmentInfo.setAssignment(savedAssignment);
                assignmentInfo.setUser(new Authority().getUser());
                assignmentInfo.setProcessDate(new Date());
                assignmentInfo.setTaskInfoStatus(TaskStatus.NEW.name());
                assignmentInfoRepository.save(assignmentInfo);
                //vazifa yuborish qismi
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom(assignment.getOwnerAssignment().getEmail());
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("Sizga yangi vazifa yuborildi");
                mailMessage.setText(savedAssignment.toString());
                javaMailSender.send(mailMessage);
                return new ApiResponse("Vazifa yuborildi", true);
            } catch (ParseException e) {
                return new ApiResponse("Sana noto`g'ri kiritildi: " + assignmentDto.getFinishTime(), false);
            }
        }
        return new ApiResponse("Boshqaruvchi topilmadi", false);
    }

    public ApiResponse addAssignmentToEmployees(AssignmentDto assignmentDto) {
        Optional<User> optionalUser = userRepository.findById(assignmentDto.getEmployeeId());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();  // bu user topshiriq yuborilayotgan user
            String roleName = "";
            for (Role role : user.getRoles()) {
                roleName = role.getRoleName();
            }
            if (roleName.equals(RoleName.MANAGER.name()) || roleName.equals(RoleName.HR_MANAGER.name()))
                return new ApiResponse("Siz boshqaruvchilarga vazifa bera olmaysiz", false);
            Assignment assignment = new Assignment();
            assignment.setName(assignmentDto.getName());
            assignment.setComment(assignmentDto.getComment());
            assignment.setOwnerAssignment(new Authority().getUser());
            assignment.setStatus(TaskStatus.NEW.name());
            assignment.setEmployee(user);
            try {
                String finishTime = assignmentDto.getFinishTime();
                Date finishDate = new SimpleDateFormat("dd/MM/yyyy").parse(finishTime);
                assignment.setFinishTime(finishDate);
                Assignment savedAssignment = assignmentRepository.save(assignment);
                AssignmentInfo assignmentInfo = new AssignmentInfo();
                assignmentInfo.setAssignment(savedAssignment);
                assignmentInfo.setUser(new Authority().getUser());
                assignmentInfo.setProcessDate(new Date());
                assignmentInfo.setTaskInfoStatus(TaskStatus.NEW.name());
                assignmentInfoRepository.save(assignmentInfo);
                //vazifa yuborish qismi
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom(assignment.getOwnerAssignment().getEmail());
                mailMessage.setTo(user.getEmail());
                mailMessage.setSubject("Sizga yangi vazifa yuborildi");
                mailMessage.setText(savedAssignment.toString());
                javaMailSender.send(mailMessage);
                return new ApiResponse("Vazifa yuborildi", true);
            } catch (ParseException e) {
                return new ApiResponse("Sana noto`g'ri kiritildi: " + assignmentDto.getFinishTime(), false);
            }
        }
        return new ApiResponse("Xodim topilmadi", false);
    }


    public ApiResponse acceptAssignment(UUID id) {
        Optional<Assignment> optionalAssignment = assignmentRepository.findById(id);
        if (optionalAssignment.isPresent()) {
            Assignment assignment = optionalAssignment.get();
            if (new Authority().getUser().getId().equals(assignment.getEmployee().getId())) {
                if (assignment.getStatus().equals(TaskStatus.IN_PROCESS.name()))
                    return new ApiResponse("Siz bu vazifani avval qabul qilgansiz", false);
                assignment.setStatus(TaskStatus.IN_PROCESS.name());
                Assignment save = assignmentRepository.save(assignment);
                AssignmentInfo assignmentInfo = new AssignmentInfo();
                assignmentInfo.setAssignment(save);
                assignmentInfo.setUser(save.getEmployee());
                assignmentInfo.setTaskInfoStatus(TaskInfoStatus.ACCEPTED.name());
                assignmentInfo.setProcessDate(new Date());
                assignmentInfoRepository.save(assignmentInfo);
                return new ApiResponse("Vazifa qabul qilindi", true);
            }
            return new ApiResponse("Ushbu vazifa sizga berilmagan", false);
        }
        return new ApiResponse("Bunday vazifa topilmadi", false);
    }


    public ApiResponse completeTheTask(UUID id) {
        Optional<Assignment> optionalAssignment = assignmentRepository.findById(id);
        if (optionalAssignment.isPresent()) {
            Assignment assignment = optionalAssignment.get();
            if (new Authority().getUser().getId().equals(assignment.getEmployee().getId())) {
                if (assignment.getStatus().equals(TaskStatus.DONE.name()))
                    return new ApiResponse("Siz bu vazifani bajarib bo'lgansiz", false);
                assignment.setStatus(TaskStatus.DONE.name());
                Assignment save = assignmentRepository.save(assignment);
                AssignmentInfo assignmentInfo = new AssignmentInfo();
                assignmentInfo.setAssignment(save);
                assignmentInfo.setUser(save.getEmployee());
                assignmentInfo.setTaskInfoStatus(TaskInfoStatus.DONE.name());
                assignmentInfo.setProcessDate(new Date());
                assignmentInfoRepository.save(assignmentInfo);
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom(new Authority().getUser().getEmail());
                mailMessage.setTo(assignment.getOwnerAssignment().getEmail());
                mailMessage.setSubject("Vazifa bajarildi");
                mailMessage.setText("Sizni xodimga bergan topshirig'ingiz bajarildi");
                javaMailSender.send(mailMessage);
                return new ApiResponse("Vazifa yakunlandi", true);
            }
            return new ApiResponse("Ushbu vazifa sizga berilmagan", false);
        }
        return new ApiResponse("Bunday vazifa topilmadi", false);
    }


    public List<Assignment> getMyAssignments() {
        return assignmentRepository.findAllByEmployeeIdAndStatusOrderByCreatedAtDesc(new Authority().getUser().getId(), TaskStatus.IN_PROCESS.name());
    }

    public List<Assignment> getMyNewAssignments() {
        return assignmentRepository.findAllByEmployeeIdAndStatusOrderByCreatedAtDesc(new Authority().getUser().getId(), TaskStatus.NEW.name());
    }

    public List<Assignment> getTasksIGave() {
        return assignmentRepository.findAllByOwnerAssignmentIdOrderByCreatedAtDesc(new Authority().getUser().getId());
    }

    public AssignmentInfoDto getAssignmentInfoByUserId(UUID id) {
        List<Assignment> assignmentList = assignmentRepository.findAllByEmployeeIdAndStatusOrderByCreatedAtDesc(id, TaskStatus.DONE.name());
        AssignmentInfoDto assignmentInfoDto=new AssignmentInfoDto();
        List<Assignment> vaqtidaBajarganlar=new ArrayList<>();
        List<Assignment> vaqtidaBajarmaganlar=new ArrayList<>();
        for (Assignment assignment : assignmentList) {
            long bajarilganVaqt = assignment.getCreatedAt().getTime();
            long berilganMuddat = assignment.getFinishTime().getTime();
            if (bajarilganVaqt<berilganMuddat){
                vaqtidaBajarganlar.add(assignment);
            }
            else {
                vaqtidaBajarmaganlar.add(assignment);
            }
        }
        assignmentInfoDto.setVaqtidaBajarganlar(vaqtidaBajarganlar);
        assignmentInfoDto.setVaqtidaBajarmaganlar(vaqtidaBajarmaganlar);
        return assignmentInfoDto;
    }
}
