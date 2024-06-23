package com.motorph.ems.service.impl;

import com.motorph.ems.dto.DeductionsDTO;
import com.motorph.ems.dto.mapper.DeductionsMapper;
import com.motorph.ems.model.Deductions;
import com.motorph.ems.model.Deductions.DeductionType;
import com.motorph.ems.repository.DeductionTypeRepository;
import com.motorph.ems.repository.DeductionsRepository;
import com.motorph.ems.service.DeductionsService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DeductionsServiceImpl implements DeductionsService {

    private final DeductionsRepository deductionRepository;
    private final DeductionTypeRepository typeRepository;
    private final DeductionsMapper deductionMapper;

    public DeductionsServiceImpl(
            DeductionsRepository deductionRepository,
            DeductionTypeRepository typeRepository,
            DeductionsMapper deductionMapper
    ) {
        this.deductionRepository = deductionRepository;
        this.typeRepository = typeRepository;
        this.deductionMapper = deductionMapper;
    }

    @Override
    public DeductionsDTO addDeduction(DeductionsDTO deductionDTO) {
        if (deductionRepository.existsByPayroll_PayrollId(deductionDTO.payrollId())) {
            throw new IllegalStateException(
                    "Deduction with code " + deductionDTO.deductionCode() +
                    " for payroll " + deductionDTO.payrollId() + " already exists");
        }

        Deductions deduction = deductionMapper.toEntity(deductionDTO);

        return deductionMapper.toDTO( deductionRepository.save(deduction));
    }

    @Override
    public List<DeductionsDTO> addMultipleDeductions(List<DeductionsDTO> deductionDTOs) {
        if (deductionDTOs.isEmpty()){
            throw new IllegalArgumentException("No new deduction to add");
        }

        List<Deductions> deductions = deductionRepository.findAllByPayroll_PayrollId(deductionDTOs.getFirst().payrollId());

        if (!deductions.isEmpty()) {
            //Remove any deduction that already exists
            deductionDTOs.removeIf(deduction -> deductions.stream()
                    .anyMatch(deductionEntity -> deductionEntity.getDeductionType().getDeductionCode().equals(deduction.deductionCode())));
        }
        return deductions.stream().map(deductionMapper::toDTO).toList();
    }

    @Override
    public Optional<DeductionsDTO> getDeductionById(Long deductionId) {
        return deductionRepository.findById(deductionId).map(deductionMapper::toDTO);
    }

    @Override
    public List<DeductionsDTO> getAllDeductions() {
        return deductionRepository.findAll().stream().map(deductionMapper::toDTO).toList();
    }

    @Override
    public List<Deductions> getDeductionsByTypeCode(String deductionCode) {
        return deductionRepository.findAllByDeductionType_DeductionCode(deductionCode);
    }

    @Override
    public List<DeductionsDTO> getDeductionsByEmployeeId(Long employeeId) {
        return deductionRepository.findAllByPayroll_Employee_EmployeeId(employeeId).stream().map(deductionMapper::toDTO).toList();
    }

    @Override
    public List<DeductionsDTO> getDeductionsByStartDate(LocalDate startDate) {
        return deductionRepository.findAllByPayroll_PeriodStart(startDate).stream().map(deductionMapper::toDTO).toList();
    }

    @Override
    public DeductionsDTO updateDeduction(Long deductionId, DeductionsDTO deductionDTO) {
       Deductions deduction = deductionRepository.findById(deductionId).orElseThrow(
               () -> new EntityNotFoundException("Deduction with statusId " + deductionId + " does not exist")
       );

       deductionMapper.updateFromDTO(deductionDTO, deduction);

       return deductionMapper.toDTO(deductionRepository.save(deduction));
    }

    @Override
    public DeductionType getDeductionTypeByCode(String deductionCode) {
        return typeRepository.findById(deductionCode).orElseThrow(
                () -> new EntityNotFoundException("Deduction type with code " + deductionCode + " does not exist"
        ));
    }

    @Override
    public List<DeductionType> getAllDeductionTypes() {
        return typeRepository.findAll();
    }
}
