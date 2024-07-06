package com.motorph.pms.service.impl;

import com.motorph.pms.dto.DepartmentDTO;
import com.motorph.pms.dto.mapper.DepartmentMapper;
import com.motorph.pms.repository.DepartmentRepository;
import com.motorph.pms.service.extended.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Cacheable("departments")
    @Override
    public List<DepartmentDTO> getDepartments() {
        return departmentMapper.toDTO(departmentRepository.findAll());
    }
}
