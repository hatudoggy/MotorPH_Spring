package com.motorph.ems.service;

import com.motorph.ems.dto.BenefitDTO;
import com.motorph.ems.model.Benefits.BenefitType;

import java.util.List;
import java.util.Optional;

public interface BenefitsService {

    BenefitDTO addNewBenefit(BenefitDTO benefit);

    Optional<BenefitDTO> getBenefitById(Long benefitId);

    List<BenefitDTO> getAllBenefits();

    List<BenefitDTO> getBenefitsByEmployeeId(Long employeeId);

    List<BenefitDTO> getBenefitsByEmployeeName(String firstName, String lastName);

    List<BenefitDTO> getBenefitsByPositionCode(String positionCode);

    BenefitDTO updateBenefit(Long benefitId, BenefitDTO updatedBenefit);

    void deleteBenefitById(Long benefitId);

    void deleteBenefitsByEmployeeId(Long employeeId);

    Optional<BenefitType> getBenefitTypeByBenefitId(int benefitId);

    Optional<BenefitType> getBenefitTypeByBenefit(String benefit);

    List<BenefitType> getAllBenefitTypes();
}

