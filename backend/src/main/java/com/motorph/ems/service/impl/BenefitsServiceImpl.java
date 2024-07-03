package com.motorph.ems.service.impl;

import com.motorph.ems.dto.BenefitDTO;
import com.motorph.ems.dto.BenefitTypeDTO;
import com.motorph.ems.dto.mapper.BenefitsMapper;
import com.motorph.ems.model.Benefits;
import com.motorph.ems.model.Benefits.BenefitType;
import com.motorph.ems.repository.BenefitTypeRepository;
import com.motorph.ems.repository.BenefitsRepository;
import com.motorph.ems.service.BenefitsService;
import com.motorph.ems.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
