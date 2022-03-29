package uz.pdp.hr_management1.payload;

import lombok.Getter;
import lombok.Setter;
import uz.pdp.hr_management1.entity.Assignment;

import java.util.List;

@Getter
@Setter
public class AssignmentInfoDto {
    List<Assignment> vaqtidaBajarganlar;
    List<Assignment> vaqtidaBajarmaganlar;
}
