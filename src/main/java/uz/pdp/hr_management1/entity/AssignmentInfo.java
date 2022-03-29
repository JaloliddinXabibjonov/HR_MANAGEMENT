package uz.pdp.hr_management1.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.hr_management1.entity.template.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AssignmentInfo extends BaseEntity {

    @OneToOne
    private Assignment assignment;

    @ManyToOne
    private User user;

    private String taskInfoStatus;

    private Date processDate;
}
