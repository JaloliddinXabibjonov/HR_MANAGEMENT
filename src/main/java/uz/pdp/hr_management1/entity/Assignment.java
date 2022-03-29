package uz.pdp.hr_management1.entity;

import lombok.*;
import uz.pdp.hr_management1.entity.template.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Assignment extends BaseEntity {

    @Column(nullable = false)
    private String name;    // topshiriq nomi

//    @Column(nullable = false)
    private String comment;  //topshiriqqa izoh

//    @Column(nullable = false, length = 1000)
    private Date finishTime;  // topshiriqni yakunlanish vaqti  kun/oy/yil raqamlar bilan kiritiladi


//    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "employee")
    private User employee;          // topshiriq qaysi xodimga berilayotganligi (xodimning email i kiritiladi)


//    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "owner_assignment")
    private User ownerAssignment;    //topshiriqni bajarilganligini qabul qilib oluvchining  'email'i kiritiladi

    @Column(nullable = false)
    private String status;  // topshiriqni bajarilgan yoki bajarilmaganligi holati

    @Override
    public String toString() {
        return "Topshiriq{" +
                "nomi:'" + name + '\'' +
                ", izoh:'" + comment + '\'' +
                ", bajarilish vaqti(gacha): " + finishTime +
                ", topshiriq yuboruvchi:" + ownerAssignment.getLastName()+" "+ownerAssignment.getFirstName() +
                '}';
    }
}
