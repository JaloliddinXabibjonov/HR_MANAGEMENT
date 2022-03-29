package uz.pdp.hr_management1.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class GiveSalaryDto {
    private UUID employeeId;
    private double summa;
}
