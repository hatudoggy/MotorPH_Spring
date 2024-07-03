package com.motorph.pms.service;

import com.motorph.pms.dto.EmploymentStatusDTO;

import java.util.List;
import java.util.Optional;

public interface EmploymentStatusService {

    EmploymentStatusDTO addNewStatus(EmploymentStatusDTO status);

    List<EmploymentStatusDTO> getEmploymentStatuses();

    Optional<EmploymentStatusDTO> getEmploymentStatusById(int statusId);

//    Optional<EmploymentStatusDTO> getEmploymentStatusByStatusName(String statusName);
}
