package com.motorph.pms.service.impl;

import com.motorph.pms.dto.PositionDTO;
import com.motorph.pms.dto.mapper.PositionMapper;
import com.motorph.pms.repository.PositionRepository;
import com.motorph.pms.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    @Override
    public List<PositionDTO> getPositionsByDepartment(String departmentCode) {
        return positionRepository.findAllByDepartment_DepartmentCode(departmentCode).stream()
                .map(positionMapper::toDTO).toList();
    }

    @Override
    public List<PositionDTO> getPositions() {
        return positionRepository.findAll().stream().map(positionMapper::toDTO).toList();
    }

    @Override
    public Optional<PositionDTO> getPositionByPositionCode(String positionCode) {
        return positionRepository.findById(positionCode).map(positionMapper::toDTO);
    }

}
