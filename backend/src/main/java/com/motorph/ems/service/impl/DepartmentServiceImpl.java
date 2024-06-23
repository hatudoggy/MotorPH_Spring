package com.motorph.ems.service.impl;

import com.motorph.ems.dto.DepartmentDTO;
import com.motorph.ems.dto.mapper.DepartmentMapper;
import com.motorph.ems.model.Department;
import com.motorph.ems.repository.DepartmentRepository;
import com.motorph.ems.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Autowired
    public DepartmentServiceImpl(
            DepartmentRepository departmentRepository,
            DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public DepartmentDTO addNewDepartment(DepartmentDTO departmentDTO) {
        if (departmentRepository.existsById(departmentDTO.departmentCode())) {
            throw new IllegalStateException("Department " + departmentDTO.departmentCode() + " already exists");
        }

        if (departmentRepository.findByDepartmentName(departmentDTO.department()).isPresent()) {
            throw new IllegalStateException("Department with name " + departmentDTO.department() + " already exists");
        }

        Department department = departmentMapper.toEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toDTO(savedDepartment);
    }

    @Override
    public List<DepartmentDTO> getDepartments() {
        return departmentMapper.toDTO(departmentRepository.findAll());
    }

    @Override
    public Optional<DepartmentDTO> getDepartmentById(String departmentCode) {
        return departmentRepository.findById(departmentCode)
                .map(departmentMapper::toDTO);
    }

    @Override
    public Optional<DepartmentDTO> getDepartmentByName(String departmentName) {
        return departmentRepository.findByDepartmentName(departmentName)
                .map(departmentMapper::toDTO);
    }
}
