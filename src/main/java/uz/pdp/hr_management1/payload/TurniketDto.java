package uz.pdp.hr_management1.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TurniketDto {

    private UUID turniketId;

    private boolean processType;
}
