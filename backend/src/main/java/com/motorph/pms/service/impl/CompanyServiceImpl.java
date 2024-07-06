package com.motorph.pms.service.impl;

import com.motorph.pms.dto.*;
import com.motorph.pms.dto.mapper.*;
import com.motorph.pms.repository.*;
import com.motorph.pms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "company")
@Service
public class CompanyServiceImpl implements CompanyService {

    private final EmploymentStatusRepository employmentStatusRepository;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final BenefitTypeRepository benefitTypeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveStatusRepository leaveStatusRepository;
    private final UserRoleRepository userRoleRepository;

    private final EmploymentStatusMapper employmentStatusMapper;
    private final PositionMapper positionMapper;
    private final DepartmentMapper departmentMapper;
    private final BenefitsMapper benefitTypeMapper;
    private final LeaveBalanceMapper leaveTypeMapper;
    private final LeaveRequestMapper leaveStatusMapper;
    private final UserMapper roleMapper;

    @Autowired
    public CompanyServiceImpl(
            EmploymentStatusRepository employmentStatusRepository,
            PositionRepository positionRepository,
            DepartmentRepository departmentRepository,
            BenefitTypeRepository benefitTypeRepository,
            LeaveTypeRepository leaveTypeRepository,
            LeaveStatusRepository leaveStatusRepository,
            UserRoleRepository userRoleRepository,

            EmploymentStatusMapper employmentStatusMapper,
            PositionMapper positionMapper,
            DepartmentMapper departmentMapper,
            BenefitsMapper benefitTypeMapper,
            LeaveBalanceMapper leaveTypeMapper,
            LeaveRequestMapper leaveStatusMapper,
            UserMapper roleMapper) {
        this.employmentStatusRepository = employmentStatusRepository;
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
        this.benefitTypeRepository = benefitTypeRepository;
        this.leaveStatusRepository = leaveStatusRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.userRoleRepository = userRoleRepository;
        this.employmentStatusMapper = employmentStatusMapper;
        this.positionMapper = positionMapper;
        this.departmentMapper = departmentMapper;
        this.benefitTypeMapper = benefitTypeMapper;
        this.leaveTypeMapper = leaveTypeMapper;
        this.leaveStatusMapper = leaveStatusMapper;
        this.roleMapper = roleMapper;
    }

    @Cacheable(cacheNames = "company")
    @Override
    public List<BenefitTypeDTO> getBenefitTypes() {
        return benefitTypeRepository.findAll().stream().map(benefitTypeMapper::toDTO).toList();
    }

    @Cacheable(cacheNames = "company")
    @Override
    public List<DepartmentDTO> getDepartments() {
        return departmentRepository.findAll().stream().map(departmentMapper::toDTO).toList();
    }

    @Cacheable(cacheNames = "company")
    @Override
    public List<EmploymentStatusDTO> getEmploymentStatuses() {
        return employmentStatusRepository.findAll().stream().map(employmentStatusMapper::toDTO).toList();
    }

    @Cacheable(cacheNames = "company")
    @Override
    public List<LeaveStatusDTO> getLeaveStatuses() {
        return leaveStatusRepository.findAll().stream().map(leaveStatusMapper::toDTO).toList();
    }

    @Cacheable(cacheNames = "company")
    @Override
    public List<LeaveTypeDTO> getLeaveTypes() {
        return leaveTypeRepository.findAll().stream().map(leaveTypeMapper::toDTO).toList();
    }

    @Cacheable(cacheNames = "company")
    @Override
    public List<PositionDTO> getPositions() {
        return positionRepository.findAll().stream().map(positionMapper::toDTO).toList();
    }

    @Cacheable(cacheNames = "company")
    @Override
    public List<RoleDTO> getRoles() {
        return userRoleRepository.findAll().stream().map(roleMapper::toDTO).toList();
    }
}
