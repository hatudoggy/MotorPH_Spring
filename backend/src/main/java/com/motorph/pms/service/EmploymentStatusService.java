package com.motorph.pms.service;

import com.motorph.pms.dto.EmploymentStatusDTO;

import java.util.List;
import java.util.Optional;

public interface EmploymentStatusService {

    List<EmploymentStatusDTO> getEmploymentStatuses();

    Optional<EmploymentStatusDTO> getEmploymentStatusById(int statusId);
}
