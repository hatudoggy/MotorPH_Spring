package com.motorph.pms.service;

import com.motorph.pms.dto.BenefitTypeDTO;

import java.util.List;
import java.util.Optional;

public interface BenefitsService {
    List<BenefitTypeDTO> getAllBenefitTypes();
}

