package com.motorph.pms.service;

import com.motorph.pms.dto.BenefitTypeDTO;

import java.util.List;

public interface BenefitTypesService {
    List<BenefitTypeDTO> getBenefitTypes();

    BenefitTypeDTO getBenefitType(int id);
}

