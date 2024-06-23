package com.motorph.ems.service;

import com.motorph.ems.dto.DeductionsDTO;
import com.motorph.ems.model.Deductions;
import com.motorph.ems.model.Deductions.DeductionType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DeductionsService {

    DeductionsDTO addDeduction(DeductionsDTO deductionDTO);

    List<DeductionsDTO> addMultipleDeductions(List<DeductionsDTO> deductionDTOs);

    Optional<DeductionsDTO> getDeductionById(Long deductionId);

    List<DeductionsDTO> getAllDeductions();

    List<Deductions> getDeductionsByTypeCode(String deductionCode);

    List<DeductionsDTO> getDeductionsByEmployeeId(Long employeeId);

    List<DeductionsDTO> getDeductionsByStartDate(LocalDate startDate);

    DeductionsDTO updateDeduction(Long deductionId, DeductionsDTO deductionDTO);

    DeductionType getDeductionTypeByCode(String deductionCode);

    List<DeductionType> getAllDeductionTypes();
}


