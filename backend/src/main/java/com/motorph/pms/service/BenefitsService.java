package com.motorph.pms.service;

<<<<<<< HEAD:backend/src/main/java/com/motorph/ems/service/BenefitsService.java
import com.motorph.ems.model.Benefits;
=======
import com.motorph.pms.dto.BenefitTypeDTO;
>>>>>>> UI-integration:backend/src/main/java/com/motorph/pms/service/BenefitsService.java

import java.util.List;

public interface BenefitsService{
    //BENEFITS SERVICES
    void addNewBenefit(Benefits benefit);
    List<Benefits> getAllBenefits();
    Benefits getBenefitById(Long benefitId);
    Benefits updateBenefit(Benefits updatedBenefit);
    void deleteBenefit(Long benefitId);
    List<Benefits> getAllBenefitsByEmployeeId(Long employeeId);

    void deleteBenefitsByEmployeeId(Long employeeId);
}
