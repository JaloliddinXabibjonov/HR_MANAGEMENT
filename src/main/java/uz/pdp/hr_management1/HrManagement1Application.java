package uz.pdp.hr_management1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HrManagement1Application {

    public static void main(String[] args) {
        SpringApplication.run(HrManagement1Application.class, args);
        System.out.println(System.currentTimeMillis());
    }

}
