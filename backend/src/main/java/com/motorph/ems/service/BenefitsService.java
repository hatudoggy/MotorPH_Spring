package com.motorph.ems.service;

import com.motorph.ems.dto.BenefitDTO;
import com.motorph.ems.dto.BenefitTypeDTO;
import com.motorph.ems.model.Benefits.BenefitType;

import java.util.List;
import java.util.Optional;

public interface BenefitsService {

    Optional<BenefitTypeDTO> getBenefitTypeByBenefitId(int benefitId);

//    Optional<BenefitTypeDTO> getBenefitTypeByBenefit(String benefit);

    List<BenefitTypeDTO> getAllBenefitTypes();
}

