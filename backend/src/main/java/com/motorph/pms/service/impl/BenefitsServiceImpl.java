package com.motorph.pms.service.impl;

import com.motorph.pms.dto.BenefitTypeDTO;
import com.motorph.pms.dto.mapper.BenefitsMapper;
import com.motorph.pms.repository.BenefitTypeRepository;
import com.motorph.pms.service.BenefitsService;
import com.motorph.pms.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BenefitsServiceImpl implements BenefitsService {

    private final BenefitTypeRepository typeRepository;
    private final BenefitsMapper benefitsMapper;
    private final EmployeeService employeeService;

    @Autowired
    public BenefitsServiceImpl(BenefitTypeRepository typeRepository, BenefitsMapper benefitsMapper, EmployeeService employeeService) {
        this.typeRepository = typeRepository;
        this.benefitsMapper = benefitsMapper;
        this.employeeService = employeeService;
    }

    @Cacheable(value = "benefits", key = "#benefitId")
    @Override
    public Optional<BenefitTypeDTO> getBenefitTypeByBenefitId(int benefitId) {
        return typeRepository.findById(benefitId).map(benefitsMapper::toDTO);
    }

//    @Override
//    public Optional<BenefitTypeDTO> getBenefitTypeByBenefit(String benefit) {
//        return typeRepository.findBenefitTypeByBenefit(benefit).map(benefitsMapper::toDTO);
//    }

    @Cacheable("benefits")
    @Override
    public List<BenefitTypeDTO> getAllBenefitTypes() {
        return typeRepository.findAll().stream()
                .map(benefitsMapper::toDTO).toList();
    }
}
