package uz.pdp.hr_management1.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class WorkerInfoDto {
    private long start;
    private long end;
    private UUID id;
}
