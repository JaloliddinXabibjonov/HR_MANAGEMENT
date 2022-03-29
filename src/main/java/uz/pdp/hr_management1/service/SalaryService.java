package uz.pdp.hr_management1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.hr_management1.entity.Salary;
import uz.pdp.hr_management1.entity.User;
import uz.pdp.hr_management1.payload.GiveSalaryDto;
import uz.pdp.hr_management1.payload.template.ApiResponse;
import uz.pdp.hr_management1.repository.SalaryRepository;
import uz.pdp.hr_management1.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SalaryService {

    @Autowired
    SalaryRepository salaryRepository;
    @Autowired
    UserRepository userRepository;
    public ApiResponse giveSalary(GiveSalaryDto salaryDto) {
        Optional<User> optionalUser = userRepository.findById(salaryDto.getEmployeeId());
        if (optionalUser.isPresent()){
            Salary salary=new Salary();
            salary.setOwner(optionalUser.get());
            salary.setSumma(salaryDto.getSumma());
            salaryRepository.save(salary);
            return new ApiResponse("Muvaffaqiyatli bajarildi",true);
        }
        return new ApiResponse("Bunday xodim topilmadi",false);
    }

    public List<Salary> salaryInfoByDateBetween(long startDate, long endDate) {
        return salaryRepository.findAllByProcessTimeBetween(new Date(startDate), new Date(endDate));
    }

    public List<Salary> salaryInfoByUserId(UUID id) {
        return salaryRepository.findAllByOwnerId(id);
    }
}
