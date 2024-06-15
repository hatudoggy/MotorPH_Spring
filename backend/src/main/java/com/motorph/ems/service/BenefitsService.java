package com.motorph.ems.service;

import com.motorph.ems.model.Benefits;

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
