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

@CacheConfig(cacheNames = {
        "supervisors",
        "employmentStatuses",
        "positions",
        "departments",
        "benefitTypes",
        "leaveTypes",
        "leaveStatuses",
        "userRoles"})
@Service
public class CompanyServiceImpl implements CompanyService {
    private final EmployeeRepository supervisorsRepository;
    private final EmploymentStatusRepository employmentStatusRepository;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final BenefitTypeRepository benefitTypeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveStatusRepository leaveStatusRepository;
    private final UserRoleRepository userRoleRepository;

    private final EmployeeMapper employeeMapper;
    private final EmploymentStatusMapper employmentStatusMapper;
    private final PositionMapper positionMapper;
    private final DepartmentMapper departmentMapper;
    private final BenefitsMapper benefitTypeMapper;
    private final LeaveBalanceMapper leaveTypeMapper;
    private final LeaveRequestMapper leaveStatusMapper;
    private final UserMapper roleMapper;

    @Autowired
    public CompanyServiceImpl(
            EmployeeRepository supervisorsRepository,
            EmploymentStatusRepository employmentStatusRepository,
            PositionRepository positionRepository,
            DepartmentRepository departmentRepository,
            BenefitTypeRepository benefitTypeRepository,
            LeaveTypeRepository leaveTypeRepository,
            LeaveStatusRepository leaveStatusRepository,
            UserRoleRepository userRoleRepository,

            EmployeeMapper employeeMapper,
            EmploymentStatusMapper employmentStatusMapper,
            PositionMapper positionMapper,
            DepartmentMapper departmentMapper,
            BenefitsMapper benefitTypeMapper,
            LeaveBalanceMapper leaveTypeMapper,
            LeaveRequestMapper leaveStatusMapper,
            UserMapper roleMapper) {
        this.supervisorsRepository = supervisorsRepository;
        this.employmentStatusRepository = employmentStatusRepository;
        this.positionRepository = positionRepository;
        this.departmentRepository = departmentRepository;
        this.benefitTypeRepository = benefitTypeRepository;
        this.leaveStatusRepository = leaveStatusRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.userRoleRepository = userRoleRepository;
        this.employeeMapper = employeeMapper;
        this.employmentStatusMapper = employmentStatusMapper;
        this.positionMapper = positionMapper;
        this.departmentMapper = departmentMapper;
        this.benefitTypeMapper = benefitTypeMapper;
        this.leaveTypeMapper = leaveTypeMapper;
        this.leaveStatusMapper = leaveStatusMapper;
        this.roleMapper = roleMapper;
    }

    @Cacheable(cacheNames = "benefitTypes")
    @Override
    public List<BenefitTypeDTO> getBenefitTypes() {
        return benefitTypeRepository.findAll().stream().map(benefitTypeMapper::toDTO).toList();
    }

    @Cacheable(cacheNames = "benefitTypes", key = "#id")
    @Override
    public BenefitTypeDTO getBenefitType(int id) {
        return benefitTypeRepository.findById(id).map(benefitTypeMapper::toDTO).orElse(null);
    }

    @Cacheable(cacheNames = "departments")
    @Override
    public List<DepartmentDTO> getDepartments() {
        return departmentRepository.findAll().stream().map(departmentMapper::toDTO).toList();
    }

    @Cacheable(cacheNames = "departments", key = "#departmentCode")
    @Override
    public DepartmentDTO getDepartment(String departmentCode) {
        return departmentRepository.findById(departmentCode).map(departmentMapper::toDTO).orElse(null);
    }

    @Cacheable(cacheNames = "employmentStatuses")
    @Override
    public List<EmploymentStatusDTO> getEmploymentStatuses() {
        return employmentStatusRepository.findAll().stream().map(employmentStatusMapper::toDTO).toList();
    }

    @Cacheable(cacheNames = "employmentStatuses", key = "#id")
    @Override
    public EmploymentStatusDTO getEmploymentStatus(int id) {
        return employmentStatusRepository.findById(id).map(employmentStatusMapper::toDTO).orElse(null);
    }

    @Cacheable(cacheNames = "leaveStatuses")
    @Override
    public List<LeaveStatusDTO> getLeaveStatuses() {
        return leaveStatusRepository.findAll().stream().map(leaveStatusMapper::toDTO).toList();
    }

    @Cacheable(cacheNames = "leaveStatuses", key = "#id")
    @Override
    public LeaveStatusDTO getLeaveStatus(int id) {
        return leaveStatusRepository.findById(id).map(leaveStatusMapper::toDTO).orElse(null);
    }

    @Cacheable(cacheNames = "leaveTypes")
    @Override
    public List<LeaveTypeDTO> getLeaveTypes() {
        return leaveTypeRepository.findAll().stream().map(leaveTypeMapper::toDTO).toList();
    }

    @Cacheable(cacheNames = "leaveTypes", key = "#id")
    @Override
    public LeaveTypeDTO getLeaveType(int id) {
        return leaveTypeRepository.findById(id).map(leaveTypeMapper::toDTO).orElse(null);
    }

    @Cacheable(cacheNames = "positions")
    @Override
    public List<PositionDTO> getPositions() {
        return positionRepository.findAll().stream().map(positionMapper::toDTO).toList();
    }

    @Cacheable(cacheNames = "positions", key = "#positionCode")
    @Override
    public PositionDTO getPosition(String positionCode) {
        return positionRepository.findById(positionCode).map(positionMapper::toDTO).orElse(null);
    }

    @Cacheable(cacheNames = "userRoles")
    @Override
    public List<RoleDTO> getRoles() {
        return userRoleRepository.findAll().stream().map(roleMapper::toDTO).toList();
    }

    @Cacheable(cacheNames = "userRoles", key = "#id")
    @Override
    public RoleDTO getRole(int id) {
        return userRoleRepository.findById(id).map(roleMapper::toDTO).orElse(null);
    }

    @Cacheable(cacheNames = "supervisors")
    @Override
    public List<SupervisorDTO> getSupervisors() {
        return supervisorsRepository.findAllByPosition_isLeader(true).stream().map(employeeMapper::toSupervisorDTO).toList();
    }

    @Cacheable(cacheNames = "supervisors", key = "#supervisorId")
    @Override
    public SupervisorDTO getSupervisor(Long supervisorId) {
        return supervisorsRepository.findById(supervisorId).map(employeeMapper::toSupervisorDTO).orElse(null);
    }
}
