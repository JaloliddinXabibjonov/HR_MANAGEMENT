package uz.pdp.hr_management1.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class AssignmentDto {

    private String name;
    private String comment;
    private String finishTime;  // topshiriqni yakunlanish vaqti  kun/oy/yil raqamlar bilan kiritiladi
    private UUID employeeId;   // topshiriq qaysi xodimga berilayotganligi (xodimning email i kiritiladi)
//    private String ownerAssignment;    //topshiriqni bajarilganligini qabul qilib oluvchining  'email'i kiritiladi

}
