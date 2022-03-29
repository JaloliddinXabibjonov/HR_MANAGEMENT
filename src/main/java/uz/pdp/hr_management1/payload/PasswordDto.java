package uz.pdp.hr_management1.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {
    private String password;   //userning tizimga ornatadigan paroli
    private String rePassword; //userning tizimga ornatadigan parolining takrori

}
