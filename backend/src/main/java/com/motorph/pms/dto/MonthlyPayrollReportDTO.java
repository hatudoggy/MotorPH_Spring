package com.motorph.pms.dto;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/dto/MonthlyPayrollReportDTO.java
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
=======
import lombok.Builder;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/dto/MonthlyPayrollReportDTO.java

import java.time.LocalDate;

@Getter @Setter @AllArgsConstructor
public class MonthlyPayrollReportDTO {
    private LocalDate month;
    private double totalEarnings;
    private double totalDeductions;
}
