package uz.pdp.hr_management1.payload;

import lombok.Getter;
import lombok.Setter;
import uz.pdp.hr_management1.entity.Assignment;
import uz.pdp.hr_management1.entity.WorkingTime;

import java.util.List;

@Getter
@Setter
public class WorkerInfo {
    List<WorkingTime> workingTimes;
    List<Assignment> assignmentInfos;
}
