package com.motorph.ems.service;

import com.motorph.pms.model.GovernmentId;

import java.util.List;

public interface GovernmentIdService {
    GovernmentId addNewGovernmentId(GovernmentId governmentId);
    List<GovernmentId> getAllGovernmentIdByEmployeeId(Long employeeId);
    GovernmentId updateGovernmentId(GovernmentId governmentId);
    void deleteGovernmentId(Long governmentId);
}
