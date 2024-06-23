package com.motorph.ems.service.impl;

import com.motorph.ems.dto.BenefitDTO;
import com.motorph.ems.dto.mapper.BenefitsMapper;
import com.motorph.ems.model.Benefits;
import com.motorph.ems.model.Benefits.BenefitType;
import com.motorph.ems.repository.BenefitTypeRepository;
import com.motorph.ems.repository.BenefitsRepository;
import com.motorph.ems.service.BenefitsService;
import com.motorph.ems.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BenefitsServiceImpl implements BenefitsService {

    private final BenefitsRepository benefitsRepository;
    private final BenefitTypeRepository typeRepository;
    private final BenefitsMapper benefitsMapper;
    private final EmployeeService employeeService;

    @Autowired
    public BenefitsServiceImpl(BenefitsRepository benefitsRepository, BenefitTypeRepository typeRepository, BenefitsMapper benefitsMapper, EmployeeService employeeService) {
        this.benefitsRepository = benefitsRepository;
        this.typeRepository = typeRepository;
        this.benefitsMapper = benefitsMapper;
        this.employeeService = employeeService;
    }

    @Override
    public BenefitDTO addNewBenefit(BenefitDTO benefitDTO) {
        if (benefitsRepository.existsByEmployee_EmployeeIdAndBenefitType_BenefitTypeId(
                benefitDTO.employeeId(), benefitDTO.benefitTypeId())) {
            throw new IllegalArgumentException("This benefit already exists for employee " + benefitDTO.employeeId());
        }
        Benefits savedBenefit = benefitsRepository.save(benefitsMapper.toEntity(benefitDTO));

        return benefitsMapper.toDTO(savedBenefit);
    }

    @Override
    public Optional<BenefitDTO> getBenefitById(Long benefitId) {
        return benefitsRepository.findById(benefitId).map(benefitsMapper::toDTO);
    }

    @Override
    public BenefitDTO updateBenefit(Long benefitsId, BenefitDTO benefitDTO) {
        Benefits existingBenefit = benefitsRepository.findById(benefitsId).orElseThrow(
                () -> new EntityNotFoundException("Benefit with statusId " + benefitsId + " does not exist"));

        benefitsMapper.updateFromDTO(benefitDTO, existingBenefit);

        return benefitsMapper.toDTO(benefitsRepository.save(existingBenefit));
    }

    @Override
    public List<BenefitDTO> getAllBenefits() {
        return benefitsRepository.findAll().stream()
                .map(benefitsMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BenefitDTO> getBenefitsByEmployeeId(Long employeeId) {
        return benefitsRepository.findAllById(Collections.singleton(employeeId)).stream()
                .map(benefitsMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BenefitDTO> getBenefitsByEmployeeName(String firstName, String lastName) {
        return benefitsRepository.findAllByEmployee_FirstNameAndEmployee_LastName(firstName,lastName).stream()
                .map(benefitsMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BenefitDTO> getBenefitsByPositionCode(String positionCode) {
        return benefitsRepository.findAllByEmployee_Position_PositionCode(positionCode).stream()
                .map(benefitsMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteBenefitById(Long benefitId) {
        if (!benefitsRepository.existsById(benefitId)) {
            throw new IllegalStateException("Benefit with statusId " + benefitId + " does not exist");
        }

        benefitsRepository.deleteById(benefitId);
    }

    @Override
    public void deleteBenefitsByEmployeeId(Long employeeId) {
        if (!benefitsRepository.existsById(employeeId)) {
            throw new IllegalStateException("Employee with statusId " + employeeId + " does not exist");
        }

        benefitsRepository.deleteAllById(Collections.singleton(employeeId));
    }

    @Override
    public Optional<BenefitType> getBenefitTypeByBenefitId(int benefitId) {
        return typeRepository.findById(benefitId);
    }

    @Override
    public Optional<BenefitType> getBenefitTypeByBenefit(String benefit) {
        return typeRepository.findBenefitTypeByBenefit(benefit);
    }

    @Override
    public List<BenefitType> getAllBenefitTypes() {
        return typeRepository.findAll();
    }
}
